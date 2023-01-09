package com.ciosmak.bankapp.repository;

import com.ciosmak.bankapp.entity.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long>
{
    Optional<PaymentCard> findByCardNumber(String cardNumber);

    ArrayList<PaymentCard> findByUserId(Long userId);
}
