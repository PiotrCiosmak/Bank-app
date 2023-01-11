package com.ciosmak.bankapp.service;

import com.ciosmak.bankapp.entity.BankAccount;
import com.ciosmak.bankapp.entity.PaymentCard;
import com.ciosmak.bankapp.entity.User;
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

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PaymentCardService extends AbstractPaymentCardService
{
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
                    throw new InputMismatchException();
                }

            }
            catch (InputMismatchException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do " + amountOfBankAccounts + ".\nSpróbuj ponownie.");
                System.err.flush();
            }
            catch (Exception e)
            {
                System.err.println("BŁĄD KRYTYCZNY!!!");
                System.err.println("OPUSZCZANIE PROGRAMU");
                System.err.flush();
                System.exit(1);
            }
        }
    }

    public void showPaymentCard(PaymentCardId paymentCardId)
    {
        paymentCardRepository.getReferenceById(paymentCardId.getId()).getStatus().showPaymentCard(paymentCardId, paymentCardRepository);
    }

    public void changeLimits(PaymentCardId paymentCardId)
    {
        paymentCardRepository.getReferenceById(paymentCardId.getId()).getStatus().changeLimits(paymentCardId, paymentCardRepository);
    }

    public void blockTemporarily(PaymentCardId paymentCardId)
    {
        paymentCardRepository.getReferenceById(paymentCardId.getId()).getStatus().blockTemporarily(paymentCardId, paymentCardRepository);
    }

    public void unlock(PaymentCardId paymentCardId)
    {
        paymentCardRepository.getReferenceById(paymentCardId.getId()).getStatus().unlock(paymentCardId, paymentCardRepository);
    }

    public void blockPermanently(PaymentCardId paymentCardId)
    {
        paymentCardRepository.getReferenceById(paymentCardId.getId()).getStatus().blockPermanently(paymentCardId, paymentCardRepository);
    }

    public void changePin(PaymentCardId paymentCardId)
    {
        paymentCardRepository.getReferenceById(paymentCardId.getId()).getStatus().changePin(paymentCardId, paymentCardRepository);
    }

    public void changeContactlessTransactionOption(PaymentCardId paymentCardId)
    {
        paymentCardRepository.getReferenceById(paymentCardId.getId()).getStatus().changeContactlessTransactionOption(paymentCardId, paymentCardRepository);
    }

    public void changeMagneticStripOption(PaymentCardId paymentCardId)
    {
        paymentCardRepository.getReferenceById(paymentCardId.getId()).getStatus().changeMagneticStripOption(paymentCardId, paymentCardRepository);
    }

    public void changeTransactionsWithDdcServiceOption(PaymentCardId paymentCardId)
    {
        paymentCardRepository.getReferenceById(paymentCardId.getId()).getStatus().changeTransactionsWithDdcServiceOption(paymentCardId, paymentCardRepository);
    }

    public void changeSurchargeTransactionsOption(PaymentCardId paymentCardId)
    {
        paymentCardRepository.getReferenceById(paymentCardId.getId()).getStatus().changeSurchargeTransactionsOption(paymentCardId, paymentCardRepository);
    }

    public void changeDebitOption(PaymentCardId paymentCardId)
    {
        paymentCardRepository.getReferenceById(paymentCardId.getId()).getStatus().changeDebitOption(paymentCardId, paymentCardRepository);
    }

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

    public int getNumberOfNoPermanentlyBlockedPaymentCards(UserId userId)
    {
        ArrayList<BankAccount> bankAccountsList = bankAccountRepository.findByUserId(userId.getId());
        int numberOfNoPermanentlyBlockedPaymentCards = 0;
        for (var bankAccount: bankAccountsList)
        {
            if(!bankAccount.getPaymentCard().getStatus().toString().equals("BLOCKED_PERMANENTLY"))
            {
                numberOfNoPermanentlyBlockedPaymentCards++;
            }
        }
        return numberOfNoPermanentlyBlockedPaymentCards;
    }

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

    private boolean cardNumberIsUnique(String cardNumber)
    {
        Optional<PaymentCard> paymentCard = paymentCardRepository.findByCardNumber(cardNumber);
        return paymentCard.isEmpty();
    }

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

    private BigDecimal prepareWithdrawalFeeInPoland()
    {
        return BigDecimal.valueOf(0.00);
    }

    private BigDecimal prepareForeignWithdrawalFee()
    {
        return BigDecimal.valueOf(5.00);
    }

    private BigDecimal preparePaymentCardMaintenanceFee()
    {
        return BigDecimal.valueOf(10.00);
    }

    private Integer prepareMinimumNumberOfTransactions()
    {
        return 5;
    }

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
                    System.err.println("Nie ma takiej opcji.\nNależy wprowadzić znak 'T' lub znak 'N'.\nSpróbuj ponownie.");
                    System.err.flush();
                }
            }
            catch (Exception e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić znak 'T' lub znak 'N'.\nSpróbuj ponownie.");
                System.err.flush();
            }
        }
    }

    private BigDecimal prepareFirstMaxDebt()
    {
        return BigDecimal.valueOf(500.00);
    }

    private final UserRepository userRepository;
    private final PaymentCardRepository paymentCardRepository;
    private final BankAccountRepository bankAccountRepository;
}
