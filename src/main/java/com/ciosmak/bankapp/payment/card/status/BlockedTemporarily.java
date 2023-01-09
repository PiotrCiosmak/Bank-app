package com.ciosmak.bankapp.payment.card.status;

import com.ciosmak.bankapp.entity.PaymentCard;
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
public class BlockedTemporarily extends AbstractPaymentCardService implements PaymentCardStatus
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
        PaymentCard paymentCard = getPaymentCardById(paymentCardId, paymentCardRepository);
        Activated activated = new Activated();
        paymentCard.setStatus(activated);
        System.out.println("\n---KARTA ZOSTAŁA ODBLOKOWANA---");
    }

    @Override
    public void blockPermanently(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        PaymentCard paymentCard = getPaymentCardById(paymentCardId, paymentCardRepository);
        BlockedPermanently blockedPermanently = new BlockedPermanently();
        paymentCard.setStatus(blockedPermanently);
        System.out.println("\n---KARTA ZOSTAŁA ZABLKOWANA PERNAMETNIE---");
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
        System.out.println("\n---KARTA JEST TYMCZASOWO ZABLOKOWANA");
        System.out.println("Aby przeprowadzić jakąkolwiek akcję należy odblokować kartę");
    }
}
