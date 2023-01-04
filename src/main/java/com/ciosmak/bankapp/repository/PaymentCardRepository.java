package com.ciosmak.bankapp.repository;

import com.ciosmak.bankapp.entity.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long>
{
    PaymentCard findByCardNumber(String cardNumber);
}
