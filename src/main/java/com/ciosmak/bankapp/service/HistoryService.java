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

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class HistoryService extends AbstractService
{
    public BigDecimal getExpensesForCurrentMonth(UserId userId)
    {
        ArrayList<BankAccount> bankAccountsList = bankAccountRepository.findByUserId(userId.getId());
        ArrayList<Transfer> transfersList = new ArrayList<>();
        for (var bankAccount : bankAccountsList)
        {
            transfersList.addAll(transferRepository.findBySenderBankAccountNumber(bankAccount.getBankAccountNumber()));
        }

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

    private final BankAccountRepository bankAccountRepository;
    private final TransferRepository transferRepository;
}
