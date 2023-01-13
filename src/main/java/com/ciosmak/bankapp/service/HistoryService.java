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

    //todo
    public void showHistory(UserId userId)
    {

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

    private Long chooseOneBankAccount(UserId userId)
    {
        ArrayList<BankAccount> bankAccountsList = bankAccountRepository.findByUserId(userId.getId());
        int amountOfBankAccounts = bankAccountsList.size();
        int selectedBankAccount;

        while (true)
        {
            try
            {
                System.out.println("---WYBIERZ RACHUNEK BANKOWY---");
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
