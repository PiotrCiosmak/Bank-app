package com.ciosmak.bankapp.service;

import com.ciosmak.bankapp.entity.BankAccount;
import com.ciosmak.bankapp.entity.PaymentCard;
import com.ciosmak.bankapp.entity.User;
import com.ciosmak.bankapp.exception.FatalError;
import com.ciosmak.bankapp.exception.IllegalAnswerTrueFalseException;
import com.ciosmak.bankapp.exception.IncorrectBankAccountException;
import com.ciosmak.bankapp.payment.card.id.PaymentCardId;
import com.ciosmak.bankapp.payment.card.status.NotActivated;
import com.ciosmak.bankapp.repository.BankAccountRepository;
import com.ciosmak.bankapp.repository.PaymentCardRepository;
import com.ciosmak.bankapp.repository.UserRepository;
import com.ciosmak.bankapp.user.id.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * PaymentCardService is a service class that handles the creation and management of PaymentCard entities.
 *
 * @author Piotr Ciosmak
 * @version 1.0
 * @see AbstractPaymentCardService
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PaymentCardService extends AbstractPaymentCardService
{
    /**
     * Creates a PaymentCard entity with the provided userId. The PaymentCard entity is initialized with values such as the user's first and last name, expiry date, and various limits and fees.
     *
     * @param userId the userId of the user associated with the PaymentCard
     * @return the created PaymentCard entity
     */
    public PaymentCard createPaymentCard(UserId userId)
    {
        User user = getUserById(userId, userRepository);

        PaymentCard paymentCard = new PaymentCard();
        System.out.println("\n---TWORZENIE KARTY PŁATNICZEJ---");

        paymentCard.setFirstName(user.getPersonalData().getFirstName());

        paymentCard.setLastName(user.getPersonalData().getLastName());

        paymentCard.setExpiryDate(LocalDate.now().plusYears(5));

        NotActivated notActivated = new NotActivated();
        paymentCard.setStatus(notActivated);

        paymentCard.setPin(preparePin());

        String cardNumber;
        do
        {
            cardNumber = generateCardNumber();
        } while (!cardNumberIsUnique(cardNumber));
        paymentCard.setCardNumber(cardNumber);

        paymentCard.setVerificationValue(generateVerificationValue());

        paymentCard.setPaymentLimitPerDay(setLimitPerDay("Podaj dzienny limit płatności: "));
        paymentCard.setWithdrawLimitPerDay(setLimitPerDay("Podaj dzienny limit wypłat z bankomatów: "));
        paymentCard.setInternetTransactionLimitPerDay(setLimitPerDay("Podaj dzienny limit płatności w internecie: "));

        paymentCard.setWithdrawalFeeInPoland(prepareWithdrawalFeeInPoland());

        paymentCard.setForeignWithdrawalFee(prepareForeignWithdrawalFee());

        paymentCard.setMaintenanceFee(preparePaymentCardMaintenanceFee());

        paymentCard.setMinimumNumberOfTransactions(prepareMinimumNumberOfTransactions());

        paymentCard.setDebtBalanceIsActive(prepareIsDebtBalanceActive());

        paymentCard.setMaxDebt(prepareFirstMaxDebt());

        if (paymentCard.isDebtBalanceIsActive())
        {
            paymentCard.setDebtBalance(prepareDebtBalance(paymentCard.getMaxDebt()));
        }

        return paymentCard;
    }

    /**
     * Allows a user to choose one of their PaymentCards.
     *
     * @param userId the userId of the user associated with the PaymentCard
     * @return the id of the selected PaymentCard
     */
    public Long chooseOnePaymentCard(UserId userId)
    {
        ArrayList<BankAccount> bankAccountsList = bankAccountRepository.findByUserId(userId.getId());
        int amountOfBankAccounts = bankAccountsList.size();
        int selectedBankAccount;

        while (true)
        {
            try
            {
                System.out.println("\n---WYBIERZ RACHUNEK BANKOWY DO KTÓREGO PODPIĘTA JEST KARTA PŁATNICZA---");
                for (int i = 0; i < amountOfBankAccounts; ++i)
                {
                    System.out.println(i + 1 + ". " + bankAccountsList.get(i).getName());
                }
                System.out.print("Wybieram: ");
                selectedBankAccount = scanner.nextInt();
                selectedBankAccount--;
                scanner = new Scanner(System.in);
                if (checkIfCorrectProductIsSelected(selectedBankAccount, amountOfBankAccounts))
                {
                    return bankAccountsList.get(selectedBankAccount).getPaymentCard().getId();
                }
                else
                {
                    throw new IncorrectBankAccountException("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do " + amountOfBankAccounts + ".\nSpróbuj ponownie.\n", "");
                }

            }
            catch (IncorrectBankAccountException e)
            {
                scanner = new Scanner(System.in);
                e.show();
            }
            catch (Exception e)
            {
                FatalError.exit();
            }
        }
    }

    /**
     * This method shows PaymentCard information by PaymentCardId.
     *
     * @param paymentCardId the ID of the PaymentCard to show information of.
     */
    public void showPaymentCard(PaymentCardId paymentCardId)
    {
        paymentCardRepository.getReferenceById(paymentCardId.getId()).getStatus().showPaymentCard(paymentCardId, paymentCardRepository);
    }

    /**
     * This method allows to change the limits of the PaymentCard.
     *
     * @param paymentCardId the ID of the PaymentCard to change limits of.
     */
    public void changeLimits(PaymentCardId paymentCardId)
    {
        paymentCardRepository.getReferenceById(paymentCardId.getId()).getStatus().changeLimits(paymentCardId, paymentCardRepository);
    }

    /**
     * This method temporarily blocks the PaymentCard.
     *
     * @param paymentCardId the ID of the PaymentCard to block.
     */
    public void blockTemporarily(PaymentCardId paymentCardId)
    {
        paymentCardRepository.getReferenceById(paymentCardId.getId()).getStatus().blockTemporarily(paymentCardId, paymentCardRepository);
    }

    /**
     * This method unlocks the PaymentCard that was previously blocked.
     *
     * @param paymentCardId the ID of the PaymentCard to unlock.
     */
    public void unlock(PaymentCardId paymentCardId)
    {
        paymentCardRepository.getReferenceById(paymentCardId.getId()).getStatus().unlock(paymentCardId, paymentCardRepository);
    }

    /**
     * This method permanently blocks the PaymentCard.
     *
     * @param paymentCardId the ID of the PaymentCard to block permanently.
     */
    public void blockPermanently(PaymentCardId paymentCardId)
    {
        paymentCardRepository.getReferenceById(paymentCardId.getId()).getStatus().blockPermanently(paymentCardId, paymentCardRepository);
    }

    /**
     * This method allows to change the PIN of the PaymentCard.
     *
     * @param paymentCardId the ID of the PaymentCard to change PIN of.
     */
    public void changePin(PaymentCardId paymentCardId)
    {
        paymentCardRepository.getReferenceById(paymentCardId.getId()).getStatus().changePin(paymentCardId, paymentCardRepository);
    }

    /**
     * This method allows to change the contactless transaction option of the PaymentCard.
     *
     * @param paymentCardId the ID of the PaymentCard to change contactless transaction option of.
     */
    public void changeContactlessTransactionOption(PaymentCardId paymentCardId)
    {
        paymentCardRepository.getReferenceById(paymentCardId.getId()).getStatus().changeContactlessTransactionOption(paymentCardId, paymentCardRepository);
    }

    /**
     * This method allows to change the magnetic strip option of the PaymentCard.
     *
     * @param paymentCardId the ID of the PaymentCard to change magnetic strip option of.
     */
    public void changeMagneticStripOption(PaymentCardId paymentCardId)
    {
        paymentCardRepository.getReferenceById(paymentCardId.getId()).getStatus().changeMagneticStripOption(paymentCardId, paymentCardRepository);
    }

    /**
     * This method allows to change the option of transactions with DDC service.
     *
     * @param paymentCardId the ID of the PaymentCard to change option of transactions with DDC service.
     */
    public void changeTransactionsWithDdcServiceOption(PaymentCardId paymentCardId)
    {
        paymentCardRepository.getReferenceById(paymentCardId.getId()).getStatus().changeTransactionsWithDdcServiceOption(paymentCardId, paymentCardRepository);
    }

    /**
     * This method allows to change the option of surcharge transactions.
     *
     * @param paymentCardId the ID of the PaymentCard to change option of surcharge transactions.
     */
    public void changeSurchargeTransactionsOption(PaymentCardId paymentCardId)
    {
        paymentCardRepository.getReferenceById(paymentCardId.getId()).getStatus().changeSurchargeTransactionsOption(paymentCardId, paymentCardRepository);
    }

    /**
     * This method allows to change the debit option of the PaymentCard.
     *
     * @param paymentCardId the ID of the PaymentCard to change debit option of.
     */
    public void changeDebitOption(PaymentCardId paymentCardId)
    {
        paymentCardRepository.getReferenceById(paymentCardId.getId()).getStatus().changeDebitOption(paymentCardId, paymentCardRepository);
    }

    /**
     * This method automatically checks the expiry date of all PaymentCards and updates it to 5 years after the current date if it has expired.
     */
    public void autoCheckExpiryDate()
    {
        ArrayList<PaymentCard> paymentCardList = paymentCardRepository.findAll();
        for (var paymentCard : paymentCardList)
        {
            if (paymentCard.getExpiryDate().isBefore(LocalDate.now()))
            {
                paymentCard.setExpiryDate(LocalDate.now().plusYears(5));
            }
        }
    }

    /**
     * This method returns the number of no permanently blocked PaymentCards by UserId.
     *
     * @param userId the ID of the user to count the number of no permanently blocked PaymentCards of.
     * @return the number of no permanently blocked PaymentCards.
     */
    public int getNumberOfNoPermanentlyBlockedPaymentCards(UserId userId)
    {
        ArrayList<BankAccount> bankAccountsList = bankAccountRepository.findByUserId(userId.getId());
        int numberOfNoPermanentlyBlockedPaymentCards = 0;
        for (var bankAccount : bankAccountsList)
        {
            if (!bankAccount.getPaymentCard().getStatus().toString().equals("BLOCKED_PERMANENTLY"))
            {
                numberOfNoPermanentlyBlockedPaymentCards++;
            }
        }
        return numberOfNoPermanentlyBlockedPaymentCards;
    }

    /**
     * This method generates a random card number for a PaymentCard.
     *
     * @return the generated card number as a string.
     */
    private String generateCardNumber()
    {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 16; i++)
        {
            cardNumber.append(random.nextInt(10));
        }
        return cardNumber.toString().trim();
    }

    /**
     * This method checks if the card number is unique.
     *
     * @param cardNumber the card number to check for uniqueness
     * @return true if the card number is unique, false otherwise
     */
    private boolean cardNumberIsUnique(String cardNumber)
    {
        Optional<PaymentCard> paymentCard = paymentCardRepository.findByCardNumber(cardNumber);
        return paymentCard.isEmpty();
    }

    /**
     * This method generates a random verification value for a PaymentCard.
     *
     * @return the generated verification value as a string.
     */
    private String generateVerificationValue()
    {
        Random random = new Random();
        StringBuilder verificationValue = new StringBuilder();
        for (int i = 0; i < 3; i++)
        {
            verificationValue.append(random.nextInt(10));
        }
        return hash(verificationValue.toString().trim());
    }

    /**
     * This method prepares withdrawal fee in Poland.
     *
     * @return withdrawal fee in Poland as BigDecimal.
     */
    private BigDecimal prepareWithdrawalFeeInPoland()
    {
        return BigDecimal.valueOf(0.00);
    }

    /**
     * This method prepares foreign withdrawal fee.
     *
     * @return foreign withdrawal fee as BigDecimal.
     */
    private BigDecimal prepareForeignWithdrawalFee()
    {
        return BigDecimal.valueOf(5.00);
    }

    /**
     * This method prepares payment card maintenance fee.
     *
     * @return payment card maintenance fee as BigDecimal.
     */
    private BigDecimal preparePaymentCardMaintenanceFee()
    {
        return BigDecimal.valueOf(10.00);
    }

    /**
     * This method prepares minimum number of transactions.
     *
     * @return minimum number of transactions as Integer.
     */
    private Integer prepareMinimumNumberOfTransactions()
    {
        return 5;
    }

    /**
     * This method prepares if debt balance active.
     *
     * @return true if debt balance is active, false otherwise.
     */
    private boolean prepareIsDebtBalanceActive()
    {
        char debtBalanceIsActive;
        while (true)
        {
            try
            {
                scanner = new Scanner(System.in);
                System.out.print("Chcesz aktywować debet na karcie (T/N): ");
                debtBalanceIsActive = scanner.nextLine().charAt(0);
                if (trueOrFalseAnswerIsCorrect(debtBalanceIsActive))
                {
                    return Character.toUpperCase(debtBalanceIsActive) == 'T';
                }
                else
                {
                    throw new IllegalAnswerTrueFalseException("Nie ma takiej opcji.\nNależy wprowadzić znak 'T' lub znak 'N'.\nSpróbuj ponownie.\n", "");
                }
            }
            catch (IllegalAnswerTrueFalseException e)
            {
                scanner = new Scanner(System.in);
                e.show();
            }
            catch (Exception e)
            {
                FatalError.exit();
            }
        }
    }

    /**
     * This method prepares the first max debt amount for a PaymentCard.
     *
     * @return the first max debt amount as a BigDecimal.
     */
    private BigDecimal prepareFirstMaxDebt()
    {
        return BigDecimal.valueOf(500.00);
    }


    /**
     * userRepository is an instance variable of type UserRepository, used to access and manipulate user data in the database.
     */
    private final UserRepository userRepository;

    /**
     * paymentCardRepository is an instance variable of type PaymentCardRepository, used to access and manipulate payment card data in the database.
     */
    private final PaymentCardRepository paymentCardRepository;

    /**
     * bankAccountRepository is an instance variable of type BankAccountRepository, used to access and manipulate bank account data in the database.
     */
    private final BankAccountRepository bankAccountRepository;
}
