package com.ciosmak.bankapp.repository;

import com.ciosmak.bankapp.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long>
{
}
