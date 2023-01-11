package com.ciosmak.bankapp.service;

import com.ciosmak.bankapp.bank.account.id.BankAccountId;
import com.ciosmak.bankapp.entity.BankAccount;
import com.ciosmak.bankapp.entity.PaymentCard;
import com.ciosmak.bankapp.entity.User;
import com.ciosmak.bankapp.repository.BankAccountRepository;
import com.ciosmak.bankapp.repository.UserRepository;
import com.ciosmak.bankapp.user.id.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

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

        String name = createBankAccountName("Podaj nazwę rachunku: ");

        PaymentCard paymentCard = paymentCardService.createPaymentCard(userId);

        User user = getUserById(userId, userRepository);

        BankAccount bankAccount = BankAccount.builder().
                balance(BigDecimal.valueOf(0.0)).
                bankAccountNumber(bankAccountNumber).
                internationalBankAccountNumber(prepareInternationalBankAccountNumber(bankAccountNumber)).
                name(name).
                paymentCard(paymentCard).
                bankIdentificationCode(prepareIdentificationCode()).
                isOpen(true).
                maintenanceFee(prepareBankAccountMaintenanceFee()).
                interest(prepareInterest()).user(user).
                build();
        bankAccountRepository.save(bankAccount);
    }

    public Long chooseOneBankAccount(UserId userId)
    {
        ArrayList<BankAccount> bankAccountsList = bankAccountRepository.findByUserId(userId.getId());
        int amountOfBankAccounts = bankAccountsList.size();
        int selectedBankAccount;

        while (true)
        {
            try
            {
                System.out.println("\n---WYBIERZ RACHUNEK BANKOWY---");
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
                    return bankAccountsList.get(selectedBankAccount).getId();
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

    public void showBankAccount(BankAccountId bankAccountId)
    {
        BankAccount bankAccount = getBankAccountById(bankAccountId, bankAccountRepository);
        System.out.println("\n---RACHUNEK BANKOWY---");
        System.out.println("Nazwa rachunku: " + bankAccount.getName());
        System.out.println("Dostępne środki: " + bankAccount.getBalance());
        System.out.println("Number konta bankowego: " + bankAccount.getBankAccountNumber());
        System.out.println(showIfBankAccountIsOpen(bankAccount.isOpen()));
        System.out.println("Miesięczne utrzymanie rachunku kosztuję: " + bankAccount.getMaintenanceFee() + "zł");
        System.out.println("Oprocenowanie w skali miesiąca wynosi: " + bankAccount.getInterest() + "%");
    }

    public void changeBankAccountName(BankAccountId bankAccountId)
    {
        BankAccount bankAccount = getBankAccountById(bankAccountId, bankAccountRepository);
        System.out.println("\n---ZMIANA NAZWY RACHUNKU BANKOWEGO---");
        System.out.println("Aktualna nazwa rachunku: " + bankAccount.getName());
        String newName = createBankAccountName("Podaj nową nazwę rachunku: ");
        bankAccount.setName(newName);
    }

    public BigDecimal getBalanceFromAllBankAccounts(UserId userId)
    {
        ArrayList<BankAccount> bankAccountsList = bankAccountRepository.findByUserId(userId.getId());
        BigDecimal balanceFromAllBankAccounts = new BigDecimal("0.0");
        for (var bankAccount : bankAccountsList)
        {
            if (bankAccount.isOpen())
            {
                balanceFromAllBankAccounts = balanceFromAllBankAccounts.add(bankAccount.getBalance());
            }
        }
        return balanceFromAllBankAccounts;
    }

    public int getNumberOfOpenBankAccounts(UserId userId)
    {
        ArrayList<BankAccount> bankAccountsList = bankAccountRepository.findByUserId(userId.getId());
        int numberOfOpenBankAccounts = 0;
        for (var bankAccount : bankAccountsList)
        {
            if (bankAccount.isOpen())
            {
                numberOfOpenBankAccounts++;
            }
        }
        return numberOfOpenBankAccounts;
    }

    private String createBankAccountName(String message)
    {
        System.out.print(message);
        return scanner.nextLine();
    }

    private String showIfBankAccountIsOpen(boolean isOpen)
    {
        if (isOpen)
        {
            return "Rachunek jest otwarty";
        }
        return "Rachunek jest zamkniety";
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

    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final PaymentCardService paymentCardService;
}
