package com.ciosmak.bankapp.repository;

import com.ciosmak.bankapp.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

/**
 * The BankAccountRepository interface is a Spring Data JPA repository for {@link BankAccount} entities.
 * It extends the {@link JpaRepository} interface and provides methods for common CRUD operations on bank accounts.
 * In addition to the standard CRUD operations, this interface also provides methods for finding a bank account by its unique id, bank account number, or user id.
 * This interface also enables the use of Spring Data specific methods for querying the database.
 *
 * @author Author Piotr Ciosmak
 * @version 1.0
 * @see JpaRepository
 * @see BankAccount
 */
public interface BankAccountRepository extends JpaRepository<BankAccount, Long>
{
    /**
     * Find bank account by id.
     *
     * @param bankAccountId the id of the bank account
     * @return the bank account with the given id
     */
    @Override
    Optional<BankAccount> findById(Long bankAccountId);

    /**
     * Find bank account by bank account number.
     *
     * @param bankAccountNumber the bank account number of the bank account
     * @return the bank account with the given bank account number
     */
    Optional<BankAccount> findByBankAccountNumber(String bankAccountNumber);

    /**
     * Find bank account by user id.
     *
     * @param userId the id of the user
     * @return the list of bank accounts that belongs to the given user
     */
    ArrayList<BankAccount> findByUserId(Long userId);
}
