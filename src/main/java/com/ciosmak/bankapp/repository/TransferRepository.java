package com.ciosmak.bankapp.repository;

import com.ciosmak.bankapp.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface TransferRepository extends JpaRepository<Transfer, Long>
{
    ArrayList<Transfer> findAll();

    ArrayList<Transfer> findBySenderBankAccountNumber(String senderBankAccountNumber);
}
