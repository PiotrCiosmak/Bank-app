package com.ciosmak.bankapp.service;

import com.ciosmak.bankapp.entity.BankAccount;
import com.ciosmak.bankapp.entity.Transfer;
import com.ciosmak.bankapp.exception.FatalError;
import com.ciosmak.bankapp.exception.IncorrectBankAccountException;
import com.ciosmak.bankapp.repository.BankAccountRepository;
import com.ciosmak.bankapp.repository.TransferRepository;
import com.ciosmak.bankapp.user.id.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Scanner;

/**
 * This service is responsible for providing history-related functionalities such as getting expenses and income for current month, and showing the history of all transactions made by the user.
 *
 * @author Piotr Ciosmak
 * @version 1.0
 * @see AbstractService
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class HistoryService extends AbstractService
{
    /**
     * Returns the total expenses made by the user for the current month.
     *
     * @param userId The user's unique identifier.
     * @return The total expenses made by the user for the current month.
     */
    public BigDecimal getExpensesForCurrentMonth(UserId userId)
    {
        ArrayList<Transfer> transfersList = getAllSenderTransfersByUserId(userId);

        BigDecimal expensesForCurrentMonth = BigDecimal.ZERO;
        for (var trans : transfersList)
        {
            if (trans.getExecutionDate().getMonth().equals(LocalDateTime.now().getMonth()))
            {
                expensesForCurrentMonth = expensesForCurrentMonth.add(trans.getAmountOfMoney());
            }
        }

        return expensesForCurrentMonth;
    }


    /**
     * Returns the total amount of money received by the user in the current month
     *
     * @param userId The ID of the user
     * @return The total amount of money received by the user in the current month
     */
    public BigDecimal getIncomeForCurrentMonth(UserId userId)
    {
        ArrayList<Transfer> transfersList = getAllReceivingTransfersByUserId(userId);

        BigDecimal expensesForCurrentMonth = BigDecimal.ZERO;
        for (var trans : transfersList)
        {
            if (trans.getExecutionDate().getMonth().equals(LocalDateTime.now().getMonth()))
            {
                expensesForCurrentMonth = expensesForCurrentMonth.add(trans.getAmountOfMoney());
            }
        }

        return expensesForCurrentMonth;
    }

    /**
     * The {@code showHistory} method is used to display the transaction history for a specific user.
     * The method takes in a {@code UserId} object as a parameter and retrieves all bank accounts associated with that user.
     * The user is then prompted to select a specific bank account to view the transaction history for, or can choose to view the history for all accounts.
     * The method then retrieves all transactions associated with the selected bank account(s) and displays them in a formatted manner, including the transaction title, amount, date, and destination account information.
     * If there are no transactions found for the selected bank account(s), the method will display a message indicating that there are no transactions.
     *
     * @param userId          a UserId object representing the user whose transaction history is to be displayed
     * @param numberOfRecords a number of record to show
     */
    public void showHistory(UserId userId, int numberOfRecords)
    {
        ArrayList<BankAccount> bankAccountsList = bankAccountRepository.findByUserId(userId.getId());
        ArrayList<Transfer> transfersList;
        ArrayList<LocalDateTime> dates = new ArrayList<>();
        ArrayList<String> destinationBankAccountsInfo = new ArrayList<>();
        char sign;
        String data;
        String destinationBankAccountInfo = "";
        Optional<BankAccount> destinationBankAccount;

        Long selectedBankAccountId;
        if (numberOfRecords == Integer.MAX_VALUE)
        {
            selectedBankAccountId = chooseOneBankAccount(bankAccountsList);
        }
        else
        {
            selectedBankAccountId = 0L;
        }
        if (selectedBankAccountId == 0L)
        {
            boolean transferListIsEmpty = true;
            for (var bankAccount : bankAccountsList)
            {
                transfersList = transferRepository.findByBankAccountsIdOrReceivingBankAccountNumber(bankAccount.getId(), bankAccount.getBankAccountNumber());
                for (var transfer : transfersList)
                {
                    transferListIsEmpty = false;
                    if (transfer.getSenderBankAccountNumber().equals(bankAccount.getBankAccountNumber()))
                    {
                        sign = '-';
                        data = Integer.toString(transfer.getExecutionDate().getDayOfMonth()) + '.' + transfer.getExecutionDate().getMonthValue() + '.' + transfer.getExecutionDate().getYear();
                        destinationBankAccountInfo = "z rachunku o nazwie: " + bankAccount.getName();
                        dates.add(transfer.getPostingDate());
                    }
                    else
                    {
                        sign = '+';
                        data = Integer.toString(transfer.getPostingDate().getDayOfMonth()) + '.' + transfer.getPostingDate().getMonthValue() + '.' + transfer.getPostingDate().getYear();
                        destinationBankAccount = bankAccountRepository.findByBankAccountNumber(transfer.getReceivingBankAccountNumber());
                        if (destinationBankAccount.isPresent())
                        {
                            destinationBankAccountInfo = "na rachunek o nazwie: " + destinationBankAccount.get().getName();
                            dates.add(transfer.getPostingDate());
                        }
                        else
                        {
                            FatalError.exit();
                        }
                    }
                    destinationBankAccountsInfo.add(String.format("%1$-" + 30 + "s", transfer.getTitle()) + "\t" + sign + String.format("%1$-" + 15 + "s", transfer.getAmountOfMoney()) + "\t" + String.format("%1$-" + 10 + "s", data) + "\t" + destinationBankAccountInfo);
                }
            }
            if (transferListIsEmpty)
            {
                System.out.println("Brak transakcji");
            }
        }
        else
        {
            Optional<BankAccount> bankAccount = bankAccountRepository.findById(selectedBankAccountId);
            if (bankAccount.isPresent())
            {
                transfersList = transferRepository.findByBankAccountsIdOrReceivingBankAccountNumber(bankAccount.get().getId(), bankAccount.get().getBankAccountNumber());
                if (!transfersList.isEmpty())
                {
                    for (var transfer : transfersList)
                    {
                        if (transfer.getSenderBankAccountNumber().equals(bankAccount.get().getBankAccountNumber()))
                        {
                            sign = '-';
                            data = Integer.toString(transfer.getExecutionDate().getDayOfMonth()) + '.' + transfer.getExecutionDate().getMonthValue() + '.' + transfer.getExecutionDate().getYear();
                            destinationBankAccountInfo = "z rachunku o nazwie: " + bankAccount.get().getName();
                            dates.add(transfer.getPostingDate());
                        }
                        else
                        {
                            sign = '+';
                            data = Integer.toString(transfer.getPostingDate().getDayOfMonth()) + '.' + transfer.getPostingDate().getMonthValue() + '.' + transfer.getPostingDate().getYear();
                            destinationBankAccount = bankAccountRepository.findByBankAccountNumber(transfer.getReceivingBankAccountNumber());
                            if (destinationBankAccount.isPresent())
                            {
                                destinationBankAccountInfo = "na rachunek o nazwie: " + destinationBankAccount.get().getName();
                                dates.add(transfer.getPostingDate());
                            }
                            else
                            {
                                FatalError.exit();
                            }
                        }
                        destinationBankAccountsInfo.add((String.format("%1$-" + 30 + "s", transfer.getTitle()) + "\t" + sign + String.format("%1$-" + 15 + "s", transfer.getAmountOfMoney()) + "\t" + String.format("%1$-" + 10 + "s", data) + "\t" + destinationBankAccountInfo));
                    }
                }
                else
                {
                    System.out.println("Brak transakcji");
                }
            }
            else
            {
                FatalError.exit();
            }
        }
        if (dates.isEmpty())
        {
            return;
        }

        ArrayList<ArrayList<Object>> dateAndInfo = new ArrayList<>();
        for (int i = 0; i < dates.size(); ++i)
        {
            ArrayList<Object> list = new ArrayList<>();
            list.add(dates.get(i));
            list.add(destinationBankAccountsInfo.get(i));
            dateAndInfo.add(list);
        }

        Collections.sort(dateAndInfo, (o1, o2) -> ((LocalDateTime) o2.get(0)).compareTo((LocalDateTime) o1.get(0)));

        for (int i = 0; i < dateAndInfo.size(); ++i)
        {
            if (i == numberOfRecords)
            {
                break;
            }
            System.out.println(dateAndInfo.get(i).get(1));
        }
    }

    /**
     * Retrieves all the transfers where the user with the given userId is the sender.
     *
     * @param userId the userId of the user whose transfers are to be retrieved
     * @return a list of transfers where the user with the given userId is the sender
     */
    private ArrayList<Transfer> getAllSenderTransfersByUserId(UserId userId)
    {
        ArrayList<BankAccount> bankAccountsList = bankAccountRepository.findByUserId(userId.getId());
        ArrayList<Transfer> transfersList = new ArrayList<>();
        for (var bankAccount : bankAccountsList)
        {
            transfersList.addAll(transferRepository.findByBankAccountsId(bankAccount.getId()));
        }
        return transfersList;
    }

    /**
     * Retrieves all the transfers where the given user's bank accounts are the receiving accounts.
     * Only the transfers that are marked as done are added to the list.
     *
     * @param userId the id of the user
     * @return a list of done transfers where the user's bank accounts are the receiving accounts
     */
    private ArrayList<Transfer> getAllReceivingTransfersByUserId(UserId userId)
    {
        ArrayList<BankAccount> bankAccountsList = bankAccountRepository.findByUserId(userId.getId());
        ArrayList<Transfer> transfersList = new ArrayList<>();
        ArrayList<Transfer> transfersListTmp;
        for (var bankAccount : bankAccountsList)
        {
            transfersListTmp = (transferRepository.findByReceivingBankAccountNumber(bankAccount.getBankAccountNumber()));
            if (!transfersListTmp.isEmpty())
            {
                for (var transfer : transfersListTmp)
                {
                    if (transfer.isDone())
                    {
                        transfersList.add(transfer);
                    }
                }
            }
        }
        return transfersList;
    }

    /**
     * Allows the user to choose one bank account from a list of bank accounts.
     *
     * @param bankAccountsList the list of bank accounts from which the user can choose
     * @return the id of the selected bank account or 0 if "all" bank accounts were selected
     */
    private Long chooseOneBankAccount(ArrayList<BankAccount> bankAccountsList)
    {
        int amountOfBankAccounts = bankAccountsList.size();
        int selectedBankAccount;

        while (true)
        {
            try
            {
                System.out.println("\n---WYBIERZ RACHUNEK BANKOWY---");
                System.out.println("1. Wszystkie");
                for (int i = 0; i < amountOfBankAccounts; ++i)
                {
                    System.out.println(i + 2 + ". " + bankAccountsList.get(i).getName());
                }
                System.out.print("Wybieram: ");
                selectedBankAccount = scanner.nextInt();
                selectedBankAccount -= 2;
                if (selectedBankAccount == -1)
                {
                    return 0L;
                }
                scanner = new Scanner(System.in);
                if (checkIfCorrectProductIsSelected(selectedBankAccount, amountOfBankAccounts))
                {
                    return bankAccountsList.get(selectedBankAccount).getId();
                }
                else
                {
                    throw new IncorrectBankAccountException("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do " + (amountOfBankAccounts + 1) + ".\nSpróbuj ponownie.\n", "");
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
