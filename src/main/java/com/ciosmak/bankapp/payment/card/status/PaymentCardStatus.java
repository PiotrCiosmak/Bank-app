package com.ciosmak.bankapp.payment.card.status;

import com.ciosmak.bankapp.payment.card.id.PaymentCardId;
import com.ciosmak.bankapp.repository.PaymentCardRepository;

public interface PaymentCardStatus
{
    void showPaymentCard(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository);
    void changeLimits(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository);
    void blockTemporarily(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository);
    void unlock(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository);
    void blockPermanently(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository);
    void changePin(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository);
    void changeContactlessTransactionOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository);
    void changeMagneticStripOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository);
    void changeTransactionsWithDdcServiceOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository);
    void changeSurchargeTransactionsOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository);
    void changeDebitOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository);
}
