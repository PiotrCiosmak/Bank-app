package com.ciosmak.bankapp.service;

import com.ciosmak.bankapp.entity.BankAccount;
import com.ciosmak.bankapp.entity.Transfer;
import com.ciosmak.bankapp.exception.*;
import com.ciosmak.bankapp.repository.BankAccountRepository;
import com.ciosmak.bankapp.repository.TransferRepository;
import com.ciosmak.bankapp.user.id.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code TransferService} class is responsible for handling all transfers related operations.
 * It enables creating, displaying and managing transfers.
 *
 * @author Piotr Ciosmak
 * @version 1.0
 * @see AbstractService
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TransferService extends AbstractService
{
    /**
     * The create method is used to create a new transfer. The method prompts the user for the title of the transfer, the amount of money to be transferred, and the account number of the recipient. The method checks if the amount of money is a valid number and if the account number is correct. The method also sets the execution date and posting date of the transfer. The method also checks if there is enough money in the account to complete the transfer.
     *
     * @param userId - the userId of the user making the transfer.
     */
    public void create(UserId userId)
    {
        System.out.println("\n---WYKONAJ PRZELEW---");

        String title;
        System.out.print("Podaj tytuł przelewu: ");
        title = scanner.nextLine();


        BigDecimal amountOfMoneyToTransfer;
        while (true)
        {
            try
            {
                System.out.print("Podaj kwotę: ");
                amountOfMoneyToTransfer = scanner.nextBigDecimal();
                Pattern pattern = Pattern.compile("\\d+\\.(\\d{3,})");
                Matcher matcher = pattern.matcher(amountOfMoneyToTransfer.toString());
                if (isNumberNegative(amountOfMoneyToTransfer))
                {
                    throw new IllegalAmountException("Podana kwota jest błędna.\nKwota nie może być liczbą ujemną.\nSpróbuj ponownie.\n", "");
                }
                if (numberIsTooLong(amountOfMoneyToTransfer))
                {
                    throw new IllegalAmountException("Podana kwota jest błędna.\nKwota nie może być aż tak duża.\nSpróbuj ponownie.\n", "");
                }
                if (!matcher.find())
                {
                    break;
                }
                throw new IllegalAmountException("Podana kwota jest błędna.\nKwota powinna się być liczbą z maksymalnie dwiema cyframi po przecinku.\nSpróbuj ponownie.\n", "");
            }
            catch (IllegalAmountException e)
            {
                scanner = new Scanner(System.in);
                e.show();
            }
            catch (InputMismatchException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Podana kwota jest błędna.\nKwota powinna się być liczbą z maksymalnie dwiema cyframi po przecinku.\nSpróbuj ponownie.");
                System.err.flush();
            }
        }

        String accountNumber;
        System.out.print("Podaj numer konta odbiorcy: ");
        while (true)
        {
            scanner = new Scanner(System.in);
            accountNumber = scanner.nextLine();

            if (checkIfAccountNumberIsCorrect(accountNumber))
            {
                break;
            }
            System.err.println("Podany numer konta jest błędny.\nNumer konta powinien się składać tylko z 26 cyfr.");
            System.err.flush();
            System.out.print("Ponownie podaj numer konta odbiorcy: ");
        }

        LocalDateTime executionDate = LocalDateTime.now();

        LocalDateTime postingDate = setPostingDate(executionDate);

        Optional<BankAccount> bankAccount = bankAccountRepository.findById(chooseOneBankAccount(userId));
        if (bankAccount.isPresent())
        {
            if (isEnoughMoneyInAccount(bankAccount.get().getBalance(), amountOfMoneyToTransfer))
            {
                bankAccount.get().setBalance(bankAccount.get().getBalance().subtract(amountOfMoneyToTransfer));
                Transfer transfer = Transfer.builder().
                        title(title).
                        amountOfMoney(amountOfMoneyToTransfer).
                        senderBankAccountNumber(bankAccount.get().getBankAccountNumber()).
                        receivingBankAccountNumber(accountNumber).
                        executionDate(executionDate).
                        postingDate(postingDate).
                        build();
                transferRepository.save(transfer);
                bankAccount.get().getTransfers().add(transfer);

                System.out.println("\n---PRZELEW ZOSTAŁ WYKONANY---");
            }
            else
            {
                System.err.println("\n---PRZELEW NIE ZOSTAŁ WYKONANY PRZEZ BRAK TAKIEJ KWOTY NA KONCIE---");
            }
        }
        else
        {
            FatalError.exit();
        }


    }

    /**
     * The autoMakeTransfers method is used to automatically process all pending transfers. The method retrieves all transfers from the transfer repository and iterates through them. For each transfer that has not yet been completed and has a posting date before its execution date, the method sets the transfer to done, finds the bank account associated with the receiving account number, and adds the transferred amount of money to the balance of that account.
     */
    public void autoMakeTransfers()
    {
        ArrayList<Transfer> transfersList = transferRepository.findAll();
        for (var transfer : transfersList)
        {
            if (!transfer.isDone())
            {
                if (transfer.getPostingDate().isBefore(transfer.getExecutionDate()))
                {
                    transfer.setDone(true);
                    Optional<BankAccount> bankAccount = bankAccountRepository.findByBankAccountNumber(transfer.getReceivingBankAccountNumber());
                    bankAccount.ifPresent(account -> account.setBalance(account.getBalance().add(transfer.getAmountOfMoney())));
                }
            }
        }
    }

    /**
     * This method checks if the given account number is correct.
     *
     * @param accountNumber the account number to be checked
     * @return true if the account number is correct, false otherwise
     */
    private boolean checkIfAccountNumberIsCorrect(String accountNumber)
    {
        return accountNumber.matches("^[0-9]*$") && accountNumber.length() == 26;
    }

    /**
     * This method sets the posting date based on the execution date.
     * If the execution date falls on a weekend, the posting date will be the next Monday at 12:00:00.
     * If the execution date is before 12:00:00, the posting date will be the same day at 12:00:00.
     * If the execution date is between 12:00:00 and 16:00:00, the posting date will be the same day at 16:00:00.
     * If the execution date is after 16:00:00, the posting date will be the next day at 12:00:00.
     *
     * @param executionDate the execution date
     * @return the posting date
     */
    private LocalDateTime setPostingDate(LocalDateTime executionDate)
    {
        LocalDateTime postingDate;
        if (executionDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) || executionDate.getDayOfWeek().equals(DayOfWeek.SUNDAY))
        {
            postingDate = executionDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).withHour(12).withMinute(0).withSecond(0);
        }
        else if (executionDate.getHour() < 12)
        {
            postingDate = executionDate.withHour(12).withMinute(0).withSecond(0);
        }
        else if (executionDate.getHour() < 16)
        {
            postingDate = executionDate.withHour(16).withMinute(0).withSecond(0);
        }
        else
        {
            postingDate = executionDate.plusDays(1).withHour(12).withMinute(0).withSecond(0);
        }
        return postingDate;
    }

    /**
     * Checks if the account has enough money to perform a transfer.
     *
     * @param balance                 The current balance of the account.
     * @param amountOfMoneyToTransfer The amount of money to be transferred from the account.
     * @return True if the account has enough money to perform the transfer, false otherwise.
     */
    private boolean isEnoughMoneyInAccount(BigDecimal balance, BigDecimal amountOfMoneyToTransfer)
    {
        return balance.subtract(amountOfMoneyToTransfer).compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Allows the user to choose one bank account from a list of bank accounts associated with a user's ID.
     *
     * @param userId The ID of the user whose bank accounts are being displayed.
     * @return The ID of the chosen bank account.
     * @throws IncorrectBankAccountException if the user enters an invalid option.
     * @throws Exception                     if a fatal error occurs.
     */
    private Long chooseOneBankAccount(UserId userId)
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
     * bankAccountRepository is an instance variable of type BankAccountRepository, used to access and manipulate bank account data in the database.
     */
    private final BankAccountRepository bankAccountRepository;

    /**
     * transferRepository is an instance variable of type TransferRepository, used to access and manipulate transfer data in the database.
     */
    private final TransferRepository transferRepository;
}
