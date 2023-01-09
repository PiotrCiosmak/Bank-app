package com.ciosmak.bankapp.service;

import com.ciosmak.bankapp.entity.BankAccount;
import com.ciosmak.bankapp.entity.PaymentCard;
import com.ciosmak.bankapp.entity.User;
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
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BankAccountService extends AbstractService
{
    public void createBankAccount(UserId userId)
    {
        System.out.println("\n---OTWIERANIE RACHUNKU BANKOWEGO---");

        String bankAccountNumber;
        do
        {
            bankAccountNumber = generateBankAccountNumber();
        } while (!bankAccountNumberIsUnique(bankAccountNumber));

        String name;
        System.out.print("Podaj nazwę rachunku: ");
        name = scanner.nextLine();

        PaymentCard paymentCard = createPaymentCard(userId);

        Optional<User> user = getUserById(userId, userRepository);

        BankAccount bankAccount = BankAccount.builder().
                balance(BigDecimal.valueOf(0.0)).
                bankAccountNumber(bankAccountNumber).
                internationalBankAccountNumber(prepareInternationalBankAccountNumber(bankAccountNumber)).
                name(name).
                paymentCard(paymentCard).
                bankIdentificationCode(prepareIdentificationCode()).
                isOpen(true).
                maintenanceFee(prepareBankAccountMaintenanceFee()).
                interest(prepareInterest()).user(user.get()).
                build();
        bankAccountRepository.save(bankAccount);
    }

    private String generateBankAccountNumber()
    {
        Random random = new Random();
        StringBuilder bankAccountNumber = new StringBuilder();
        for (int i = 0; i < 26; i++)
        {
            bankAccountNumber.append(random.nextInt(10));
        }
        return bankAccountNumber.toString().trim();
    }

    private boolean bankAccountNumberIsUnique(String bankAccountNumber)
    {
        Optional<BankAccount> bankAccount = bankAccountRepository.findByBankAccountNumber(bankAccountNumber);
        return bankAccount.isEmpty();
    }

    private String prepareInternationalBankAccountNumber(String bankAccountNumber)
    {
        return "PL" + bankAccountNumber;
    }

    private PaymentCard createPaymentCard(UserId userId)
    {
        Optional<User> user = getUserById(userId, userRepository);

        PaymentCard paymentCard = new PaymentCard();
        System.out.println("---TWORZENIE KARTY PŁATNICZEJ---");

        paymentCard.setFirstName(user.get().getPersonalData().getFirstName());

        paymentCard.setLastName(user.get().getPersonalData().getLastName());

        paymentCard.setExpiryDate(LocalDate.now().plusYears(5));

        NotActivated notActivated = new NotActivated();
        paymentCard.setStatus(notActivated);

        String pin;
        System.out.print("Podaj kod pin: ");
        while (true)
        {
            pin = scanner.nextLine();
            if (pinIsCorrect(pin))
            {
                break;
            }
            else
            {
                System.err.println("Podany kod pin jest błędny.\nKod pin powinien się składać tylko z 4 cyfr.");
                System.err.flush();
                System.out.print("Ponownie podaj kodu pin: ");
            }
        }
        paymentCard.setPin(hash(pin));

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

    private String prepareIdentificationCode()
    {
        return "BREXPLPWXXX";
    }

    private BigDecimal prepareBankAccountMaintenanceFee()
    {
        return BigDecimal.valueOf(0.0);
    }

    private BigDecimal prepareInterest()
    {
        return BigDecimal.valueOf(0.0);
    }

    private boolean pinIsCorrect(String pin)
    {
        if (pin.length() == 4)
        {
            for (int i = 0; i < pin.length(); ++i)
            {
                if (!Character.isDigit(pin.charAt(i)))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
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

    private BigDecimal setLimitPerDay(String message)
    {
        BigDecimal limitPerDay;
        while (true)
        {
            try
            {
                System.out.print(message);
                limitPerDay = scanner.nextBigDecimal();
                Pattern pattern = Pattern.compile("\\d+\\.(\\d{3,})");
                Matcher matcher = pattern.matcher(limitPerDay.toString());
                if (!matcher.find())
                {
                    return limitPerDay;
                }
                throw new InputMismatchException();
            }
            catch (InputMismatchException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Podana kwota limitu jest błędna.\nKwota limitu powinna się być liczbą z maksymalnie dwiema cyframi po przecinku.\nSpróbuj ponownie.");
                System.err.flush();
            }
        }
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

    private BigDecimal prepareDebtBalance(BigDecimal maxDebt)
    {
        BigDecimal debt;
        while (true)
        {
            try
            {
                System.out.println("---USTAWIENIE BALANSU DEBETOWEGO---");
                System.out.println("Maksymalny dostępna kwota: " + maxDebt.toString() + " zł");
                System.out.print("Kwota limitu debetowego: ");
                debt = scanner.nextBigDecimal();
                Pattern pattern = Pattern.compile("\\d+\\.(\\d{3,})");
                Matcher matcher = pattern.matcher(debt.toString());
                if (checkIfDebtBalanceIsCorrect(debt, maxDebt))
                {
                    if (!matcher.find())
                    {
                        return debt;
                    }
                    throw new InputMismatchException();
                }
                System.err.println("Podana kwota limitu debetowego jest błędna.\nKwota limitu debetowego powinna się być liczbą z zakresu od 0 do " + maxDebt.toString() + ".\nSpróbuj ponownie.");
                System.err.flush();
            }
            catch (InputMismatchException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Podana kwota limitu debetowego jest błędna.\nKwota limitu debetowego powinna się być liczbą z maksymalnie dwiema cyframi po przecinku.\nSpróbuj ponownie.");
                System.err.flush();
            }
        }
    }

    private boolean checkIfDebtBalanceIsCorrect(BigDecimal debt, BigDecimal maxDebt)
    {
        return debt.compareTo(BigDecimal.valueOf(0)) >= 0 && debt.compareTo(maxDebt) <= 0;
    }

    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final PaymentCardRepository paymentCardRepository;
}
