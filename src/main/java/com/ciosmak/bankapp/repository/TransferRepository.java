package com.ciosmak.bankapp.repository;

import com.ciosmak.bankapp.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

/**
 * The PersonalDataRepository interface is a Spring Data JPA repository for {@link TransferRepository} entities.
 * It extends the {@link JpaRepository} interface and provides methods for common CRUD operations on identity documents.
 * This interface also enables the use of Spring Data specific methods for querying the database.
 *
 * @author Piotr Ciosmak
 * @version 1.0
 * @see JpaRepository
 * @see TransferRepository
 */
public interface TransferRepository extends JpaRepository<Transfer, Long>
{
    /**
     * Method to find all transfers.
     *
     * @return ArrayList of Transfer.
     */
    ArrayList<Transfer> findAll();

    /**
     * Method to find transfers by receiving bank account number.
     *
     * @param receivingBankAccountNumber Receiving bank account number.
     * @return ArrayList of Transfer.
     */
    ArrayList<Transfer> findByReceivingBankAccountNumber(String receivingBankAccountNumber);


    /**
     * Method to find transfers by bank account id or receiving bank account number.
     *
     * @param bankAccountId              Bank account id.
     * @param receivingBankAccountNumber Receiving bank account number.
     * @return ArrayList of Transfer.
     */
    ArrayList<Transfer> findByBankAccountsIdOrReceivingBankAccountNumber(Long bankAccountId, String receivingBankAccountNumber);

    /**
     * Method to find transfers by bank account id.
     *
     * @param bankAccountId Bank account id.
     * @return ArrayList of Transfer.
     */
    ArrayList<Transfer> findByBankAccountsId(Long bankAccountId);
}
