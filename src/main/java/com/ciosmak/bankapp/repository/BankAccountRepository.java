package com.ciosmak.bankapp.repository;

import com.ciosmak.bankapp.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long>
{
    BankAccount findByBankAccountNumber(String bankAccountNumber);
}
