package com.ciosmak.bankapp.repository;

import com.ciosmak.bankapp.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long>
{
    Optional<BankAccount> findByBankAccountNumber(String bankAccountNumber);

    ArrayList<BankAccount> findByUserId(Long userId);
}
