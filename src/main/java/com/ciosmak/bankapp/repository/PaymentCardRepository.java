package com.ciosmak.bankapp.repository;

import com.ciosmak.bankapp.entity.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

/**
 * The PaymentCardRepository interface is a Spring Data JPA repository for {@link PaymentCard} entities.
 * It extends the {@link JpaRepository} interface and provides methods for common CRUD operations on payment cards.
 * In addition to the standard CRUD operations, this interface also provides methods for finding a payment card by its unique id, or card number.
 * This interface also enables the use of Spring Data specific methods for querying the database.
 *
 * @author Piotr Ciosmak
 * @version 1.0
 * @see JpaRepository
 * @see PaymentCard
 */
public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long>
{

    /**
     * Finds a payment card by its unique id
     *
     * @param paymentCardId the id of the payment card
     * @return the payment card with the given id
     */
    @Override
    Optional<PaymentCard> findById(Long paymentCardId);

    /**
     * Retrieves all payment cards from the repository.
     *
     * @return list of all payment cards
     */
    ArrayList<PaymentCard> findAll();

    /**
     * Finds a payment card by its card number.
     *
     * @param cardNumber the card number of the payment card
     * @return the payment card with the given card number
     */
    Optional<PaymentCard> findByCardNumber(String cardNumber);
}
