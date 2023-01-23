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

    private final BankAccountRepository bankAccountRepository;
    private final TransferRepository transferRepository;
}
