package com.ciosmak.bankapp.service;

import com.ciosmak.bankapp.entity.BankAccount;
import com.ciosmak.bankapp.entity.Transfer;
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
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class HistoryService extends AbstractService
{
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

    public void showHistory(UserId userId)
    {
        ArrayList<BankAccount> bankAccountsList = bankAccountRepository.findByUserId(userId.getId());
        ArrayList<Transfer> transfersList;
        char sign;
        String data;
        String destinationBankAccountInfo = "";
        Optional<BankAccount> destinationBankAccount;

        Long selectedBankAccountId = chooseOneBankAccount(bankAccountsList);
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
                    }
                    else
                    {
                        sign = '+';
                        data = Integer.toString(transfer.getPostingDate().getDayOfMonth()) + '.' + transfer.getPostingDate().getMonthValue() + '.' + transfer.getPostingDate().getYear();
                        destinationBankAccount = bankAccountRepository.findByBankAccountNumber(transfer.getReceivingBankAccountNumber());
                        if (destinationBankAccount.isPresent())
                        {
                            destinationBankAccountInfo = "na rachunek o nazwie: " + destinationBankAccount.get().getName();
                        }
                        else
                        {
                            System.err.println("BŁĄD KRYTYCZNY!!!");
                            System.err.println("OPUSZCZANIE PROGRAMU");
                            System.err.flush();
                            System.exit(1);
                        }
                    }
                    System.out.println(String.format("%1$-" + 30 + "s", transfer.getTitle()) + "\t" + sign + String.format("%1$-" + 15 + "s", transfer.getAmountOfMoney()) + "\t" + String.format("%1$-" + 10 + "s", data) + "\t" + destinationBankAccountInfo);
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
                        }
                        else
                        {
                            sign = '+';
                            data = Integer.toString(transfer.getPostingDate().getDayOfMonth()) + '.' + transfer.getPostingDate().getMonthValue() + '.' + transfer.getPostingDate().getYear();
                            destinationBankAccount = bankAccountRepository.findByBankAccountNumber(transfer.getReceivingBankAccountNumber());
                            if (destinationBankAccount.isPresent())
                            {
                                destinationBankAccountInfo = "na rachunek o nazwie: " + destinationBankAccount.get().getName();
                            }
                            else
                            {
                                System.err.println("BŁĄD KRYTYCZNY!!!");
                                System.err.println("OPUSZCZANIE PROGRAMU");
                                System.err.flush();
                                System.exit(1);
                            }
                        }
                        System.out.println(String.format("%1$-" + 30 + "s", transfer.getTitle()) + "\t" + sign + String.format("%1$-" + 15 + "s", transfer.getAmountOfMoney()) + "\t" + String.format("%1$-" + 10 + "s", data) + "\t" + destinationBankAccountInfo);
                    }
                }
                else
                {
                    System.out.println("Brak transakcji");
                }
            }
            else
            {
                System.err.println("BŁĄD KRYTYCZNY!!!");
                System.err.println("OPUSZCZANIE PROGRAMU");
                System.err.flush();
                System.exit(1);
            }
        }
    }

    public void showLastFiveTransactions(UserId userId)
    {
        ArrayList<BankAccount> bankAccountsList = bankAccountRepository.findByUserId(userId.getId());
        ArrayList<Transfer> transfersList;
        char sign;
        String data;
        String destinationBankAccountInfo = "";
        Optional<BankAccount> destinationBankAccount;
        int counter = 0;

        boolean transferListIsEmpty = true;
        for (var bankAccount : bankAccountsList)
        {
            transfersList = transferRepository.findByBankAccountsIdOrReceivingBankAccountNumber(bankAccount.getId(), bankAccount.getBankAccountNumber());
            for (var transfer : transfersList)
            {
                if (counter == 5)
                {
                    return;
                }
                transferListIsEmpty = false;
                if (transfer.getSenderBankAccountNumber().equals(bankAccount.getBankAccountNumber()))
                {
                    sign = '-';
                    data = Integer.toString(transfer.getExecutionDate().getDayOfMonth()) + '.' + transfer.getExecutionDate().getMonthValue() + '.' + transfer.getExecutionDate().getYear();
                    destinationBankAccountInfo = "z rachunku o nazwie: " + bankAccount.getName();
                }
                else
                {
                    sign = '+';
                    data = Integer.toString(transfer.getPostingDate().getDayOfMonth()) + '.' + transfer.getPostingDate().getMonthValue() + '.' + transfer.getPostingDate().getYear();
                    destinationBankAccount = bankAccountRepository.findByBankAccountNumber(transfer.getReceivingBankAccountNumber());
                    if (destinationBankAccount.isPresent())
                    {
                        destinationBankAccountInfo = "na rachunek o nazwie: " + destinationBankAccount.get().getName();
                    }
                    else
                    {
                        System.err.println("BŁĄD KRYTYCZNY!!!");
                        System.err.println("OPUSZCZANIE PROGRAMU");
                        System.err.flush();
                        System.exit(1);
                    }
                }
                System.out.println(String.format("%1$-" + 30 + "s", transfer.getTitle()) + "\t" + sign + String.format("%1$-" + 15 + "s", transfer.getAmountOfMoney()) + "\t" + String.format("%1$-" + 10 + "s", data) + "\t" + destinationBankAccountInfo);
                counter++;
            }
        }
        if (transferListIsEmpty)
        {
            System.out.println("Brak transakcji");
        }
    }

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

    private final BankAccountRepository bankAccountRepository;
    private final TransferRepository transferRepository;
}
