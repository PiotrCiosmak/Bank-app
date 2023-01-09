package com.ciosmak.bankapp.payment.card.status;

import com.ciosmak.bankapp.entity.PaymentCard;
import com.ciosmak.bankapp.payment.card.id.PaymentCardId;
import com.ciosmak.bankapp.repository.PaymentCardRepository;
import com.ciosmak.bankapp.service.AbstractPaymentCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class NotActivated extends AbstractPaymentCardService implements PaymentCardStatus
{

    @Override
    public void showPaymentCard(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        activatePaymentCard(paymentCardId,paymentCardRepository);
    }

    @Override
    public void changeLimits(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        activatePaymentCard(paymentCardId,paymentCardRepository);
    }

    @Override
    public void blockTemporarily(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        activatePaymentCard(paymentCardId,paymentCardRepository);
    }

    @Override
    public void unlock(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        activatePaymentCard(paymentCardId,paymentCardRepository);
    }

    @Override
    public void blockPermanently(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        activatePaymentCard(paymentCardId,paymentCardRepository);
    }

    @Override
    public void changePin(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        activatePaymentCard(paymentCardId,paymentCardRepository);
    }

    @Override
    public void changeContactlessTransactionOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        activatePaymentCard(paymentCardId,paymentCardRepository);
    }

    @Override
    public void changeMagneticStripOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        activatePaymentCard(paymentCardId,paymentCardRepository);
    }

    @Override
    public void changeTransactionsWithDdcServiceOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        activatePaymentCard(paymentCardId,paymentCardRepository);
    }

    @Override
    public void changeSurchargeTransactionsOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        activatePaymentCard(paymentCardId, paymentCardRepository);
    }

    @Override
    public void changeDebitOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        activatePaymentCard(paymentCardId, paymentCardRepository);
    }

    private void activatePaymentCard(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        PaymentCard paymentCard = getPaymentCardById(paymentCardId, paymentCardRepository);
        String pin;
        while (true)
        {
            System.out.println("\n---KARTA PŁATNICZA NIE JEST AKTYWNA---");
            System.out.println("Aby aktywować kartę płatniczą wprowadź pin.");
            System.out.print("pin: ");
            pin = scanner.nextLine();
            if (checkIfHashedIsCorrect(pin, paymentCard.getPin()))
            {
                pauseProgram();
                Activated activated = new Activated();
                paymentCard.setStatus(activated);
                System.out.println("Karta płatnicza została aktywowana");
                break;
            }
                System.err.println("Podany kod pin jest błędny.\nKod pin powinien się składać tylko z 4 cyfr.");
                System.err.flush();
                System.out.print("Ponownie podaj kodu pin: ");
        }
    }

    private void pauseProgram()
    {
        System.out.println("Aktywacja kart. To może chwilę potrwać...");
        Random random = new Random();
        int delay = random.nextInt(11) + 5;
        try
        {
            Thread.sleep(delay * 1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
