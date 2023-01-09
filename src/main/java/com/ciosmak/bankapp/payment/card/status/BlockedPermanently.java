package com.ciosmak.bankapp.payment.card.status;

import com.ciosmak.bankapp.payment.card.id.PaymentCardId;
import com.ciosmak.bankapp.repository.PaymentCardRepository;
import com.ciosmak.bankapp.service.AbstractPaymentCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BlockedPermanently extends AbstractPaymentCardService implements PaymentCardStatus
{
    @Override
    public void showPaymentCard(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    @Override
    public void changeLimits(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    @Override
    public void blockTemporarily(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    @Override
    public void unlock(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    @Override
    public void blockPermanently(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    @Override
    public void changePin(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    @Override
    public void changeContactlessTransactionOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    @Override
    public void changeMagneticStripOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    @Override
    public void changeTransactionsWithDdcServiceOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    @Override
    public void changeSurchargeTransactionsOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    @Override
    public void changeDebitOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    private void showMessage()
    {
        System.out.println("\n---TA KARTA JEST PERMANENTNIE ZABLKOWANA");
        System.out.println("Brak możliwości przeprowadzenia jakiejkolwiek akcji");
    }
}
