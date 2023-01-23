package com.ciosmak.bankapp.service;

import com.ciosmak.bankapp.bank.account.id.BankAccountId;
import com.ciosmak.bankapp.entity.BankAccount;
import com.ciosmak.bankapp.entity.PaymentCard;
import com.ciosmak.bankapp.entity.User;
import com.ciosmak.bankapp.exception.FatalError;
import com.ciosmak.bankapp.exception.IncorrectBankAccountException;
import com.ciosmak.bankapp.repository.BankAccountRepository;
import com.ciosmak.bankapp.repository.UserRepository;
import com.ciosmak.bankapp.user.id.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Service class for handling bank account related operations.
 *
 * @author Piotr Ciosmak
 * @version 1.0
 * @see AbstractService
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BankAccountService extends AbstractService
{
    /**
     * Creates a new bank account for the user.
     *
     * @param userId the id of the user to create the bank account for
     */
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

    /**
     * Shows the details of a specific bank account.
     *
     * @param bankAccountId the id of the bank account to show details for
     */
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

    /**
     * Changes the name of a specific bank account.
     *
     * @param bankAccountId the id of the bank account to change the name of
     */
    public void changeBankAccountName(BankAccountId bankAccountId)
    {
        BankAccount bankAccount = getBankAccountById(bankAccountId, bankAccountRepository);
        System.out.println("\n---ZMIANA NAZWY RACHUNKU BANKOWEGO---");
        System.out.println("Aktualna nazwa rachunku: " + bankAccount.getName());
        String newName = createBankAccountName("Podaj nową nazwę rachunku: ");
        bankAccount.setName(newName);
    }

    /**
     * Retrieves the total balance of all bank accounts for a specific user.
     *
     * @param userId the id of the user to retrieve the total balance for
     * @return the total balance of all bank accounts for the user
     */
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

    /**
     * Returns the number of open bank accounts for a user.
     *
     * @param userId the ID of the user to count the number of open bank accounts
     * @return the number of open bank accounts for the user
     */
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

    /**
     * Allows the user to choose one bank account from a list of bank accounts associated with a user.
     *
     * @param userId the ID of the user whose bank accounts will be listed
     * @return the ID of the selected bank account
     */
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
     * Creates a new bank account name based on user input.
     *
     * @param message message to be displayed to the user
     * @return the new bank account name
     */
    private String createBankAccountName(String message)
    {
        System.out.print(message);
        return scanner.nextLine();
    }

    /**
     * Show if the bank account is open or closed.
     *
     * @param isOpen status of bank account
     * @return message with information about bank account status
     */
    private String showIfBankAccountIsOpen(boolean isOpen)
    {
        if (isOpen)
        {
            return "Rachunek jest otwarty";
        }
        return "Rachunek jest zamkniety";
    }

    /**
     * Generates a unique bank account number.
     *
     * @return unique bank account number
     */
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

    /**
     * The method checks whether the bank account number is unique.
     * It uses bankAccountRepository to find a bank account with the given number.
     * If the account is found, the method returns false. If the account is not found, the method returns true.
     *
     * @param bankAccountNumber String representation of bank account number that needs to be checked
     * @return boolean representation of whether the bank account number is unique
     */
    private boolean bankAccountNumberIsUnique(String bankAccountNumber)
    {
        Optional<BankAccount> bankAccount = bankAccountRepository.findByBankAccountNumber(bankAccountNumber);
        return bankAccount.isEmpty();
    }

    /**
     * This method is used to prepare the international bank account number.
     * <p>
     * It adds "PL" prefix to the given bank account number.
     *
     * @param bankAccountNumber The bank account number
     * @return Returns the international bank account number.
     */
    private String prepareInternationalBankAccountNumber(String bankAccountNumber)
    {
        return "PL" + bankAccountNumber;
    }

    /**
     * This method is used to prepare the bank identification code.
     * It returns the hardcoded value "BREXPLPWXXX"
     *
     * @return Returns the bank identification code
     */
    private String prepareIdentificationCode()
    {
        return "BREXPLPWXXX";
    }

    /**
     * Prepares the bank account maintenance fee by returning a BigDecimal value of 0.0.
     *
     * @return A BigDecimal value of 0.0 representing the bank account maintenance fee.
     */
    private BigDecimal prepareBankAccountMaintenanceFee()
    {
        return BigDecimal.valueOf(0.0);
    }

    /**
     * The method prepareInterest() returns a BigDecimal value representing the interest rate of the bank account.
     * The default interest rate is 0.0.
     *
     * @return BigDecimal representing the interest rate of the bank account
     */
    private BigDecimal prepareInterest()
    {
        return BigDecimal.valueOf(0.0);
    }

    /**
     * userRepository is an instance variable of type UserRepository, used to access and manipulate user data in the database.
     */
    private final UserRepository userRepository;

    /**
     * bankAccountRepository is an instance variable of type BankAccountRepository, used to access and manipulate bank account data in the database.
     */
    private final BankAccountRepository bankAccountRepository;

    /**
     * Represents an instance of PaymentCardService class
     */
    private final PaymentCardService paymentCardService;
}
