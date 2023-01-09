package com.ciosmak.bankapp.payment.card.status;

import com.ciosmak.bankapp.entity.PaymentCard;
import com.ciosmak.bankapp.payment.card.id.PaymentCardId;
import com.ciosmak.bankapp.repository.PaymentCardRepository;
import com.ciosmak.bankapp.service.AbstractPaymentCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class Activated extends AbstractPaymentCardService implements PaymentCardStatus
{
    @Override
    public void showPaymentCard(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        PaymentCard paymentCard = getPaymentCardById(paymentCardId, paymentCardRepository);
        System.out.println("\n---KARTA PŁATNICZA---");
        System.out.println("Imie: " + paymentCard.getFirstName());
        System.out.println("Nazwisko: " + paymentCard.getLastName());
        System.out.println("Data wygaśnięcia: " + paymentCard.getExpiryDate().toString());
        System.out.println("Status: " + paymentCard.getStatus().toString());
        System.out.println("Dzienny limit płatności: " + paymentCard.getPaymentLimitPerDay());
        System.out.println("Dzienny limit wypłat z bankomatów: " + paymentCard.getWithdrawLimitPerDay());
        System.out.println("Dzienny limit płatności w internecie: " + paymentCard.getInternetTransactionLimitPerDay());
        System.out.print("Płatności zbliżeniowego: ");
        if (paymentCard.isContactlessTransactionsAreActive())
        {
            System.out.println("włączone");
        }
        else
        {
            System.out.println("wyłączone");
        }
        System.out.print("Pasek magnetyczny: ");
        if (paymentCard.isMagneticStripIsActive())
        {
            System.out.println("włączony");
        }
        else
        {
            System.out.println("wyłączony");
        }
        System.out.print("Usługi DDC: ");
        if (paymentCard.isTransactionsWithDdcServiceAreActive())
        {
            System.out.println("włączone");
        }
        else
        {
            System.out.println("wyłączone");
        }
        System.out.print("Transakcje z dopłatą: ");
        if (paymentCard.isSurchargeTransactionsAreActive())
        {
            System.out.println("włączone");
        }
        else
        {
            System.out.println("wyłączone");
        }
        System.out.print("Balans debetowy: ");
        if (paymentCard.isDebtBalanceIsActive())
        {
            System.out.println(paymentCard.getDebtBalance());
        }
        else
        {
            System.out.println("wyłączone");
        }
        System.out.println("Limit balansu debetowego: " + paymentCard.getMaxDebt());
    }

    @Override
    public void changeLimits(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        PaymentCard paymentCard = getPaymentCardById(paymentCardId, paymentCardRepository);
        System.out.println("\n---ZMIANA USTAWIEŃ LIMITÓW---");
        paymentCard.setPaymentLimitPerDay(setLimitPerDay("Podaj nowy dzienny limit płatności: "));
        paymentCard.setWithdrawLimitPerDay(setLimitPerDay("Podaj nowy dzienny limit wypłat z bankomatów: "));
        paymentCard.setInternetTransactionLimitPerDay(setLimitPerDay("Podaj nowy dzienny limit płatności w internecie: "));
    }

    @Override
    public void blockTemporarily(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        PaymentCard paymentCard = getPaymentCardById(paymentCardId, paymentCardRepository);
        BlockedTemporarily blockedTemporarily = new BlockedTemporarily();
        paymentCard.setStatus(blockedTemporarily);
        System.out.println("\n---KARTA ZOSTAŁA ZABLOKOWANA TYMCZASOWO---");
    }

    @Override
    public void unlock(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        System.out.println("\n---KARTA NIE JEST ZABLOKOWANA---");
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
        PaymentCard paymentCard = getPaymentCardById(paymentCardId, paymentCardRepository);
        System.out.println("\n---ZMIANA PINU---");
        paymentCard.setPin(preparePin());
    }

    @Override
    public void changeContactlessTransactionOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        PaymentCard paymentCard = getPaymentCardById(paymentCardId, paymentCardRepository);
        System.out.println("\n---ZMIANA USTAWIEŃ PŁATNOŚCI ZBLIŻENIOWYCH---");
        if (paymentCard.isContactlessTransactionsAreActive())
        {
            paymentCard.setContactlessTransactionsAreActive(false);
            System.out.println("Płatności zbliżeniowego zostały wyłączone");
        }
        else
        {
            paymentCard.setContactlessTransactionsAreActive(true);
            System.out.println("Płatności zbliżeniowego zostały włączone");
        }
    }

    @Override
    public void changeMagneticStripOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        PaymentCard paymentCard = getPaymentCardById(paymentCardId, paymentCardRepository);
        System.out.println("\n---ZMIANA USTAWIEŃ PASKA MAGNETYCZNEGO---");
        if (paymentCard.isMagneticStripIsActive())
        {
            paymentCard.setMagneticStripIsActive(false);
            System.out.println("Pasek magnetyczny został wyłączony");
        }
        else
        {
            paymentCard.setMagneticStripIsActive(true);
            System.out.println("Pasek magnetyczny został włączony");
        }
    }

    @Override
    public void changeTransactionsWithDdcServiceOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        PaymentCard paymentCard = getPaymentCardById(paymentCardId, paymentCardRepository);
        System.out.println("\n---ZMIANA USTAWIEŃ USŁUG DDC---");
        if (paymentCard.isTransactionsWithDdcServiceAreActive())
        {
            paymentCard.setTransactionsWithDdcServiceAreActive(false);
            System.out.println("Usługi DDC zostały wyłączone");
        }
        else
        {
            paymentCard.setTransactionsWithDdcServiceAreActive(true);
            System.out.println("Usługi DDC zostały włączone");
        }
    }

    @Override
    public void changeSurchargeTransactionsOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        PaymentCard paymentCard = getPaymentCardById(paymentCardId, paymentCardRepository);
        System.out.println("\n---ZMIANA USTAWIEŃ TRANSAKCJI Z DOPŁATĄ---");
        if (paymentCard.isSurchargeTransactionsAreActive())
        {
            paymentCard.setSurchargeTransactionsAreActive(false);
            System.out.println("Transakcje z dopłatą zostały wyłączone");
        }
        else
        {
            paymentCard.setSurchargeTransactionsAreActive(true);
            System.out.println("Transakcje z dopłatą zostały włączone");
        }
    }

    @Override
    public void changeDebitOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        PaymentCard paymentCard = getPaymentCardById(paymentCardId, paymentCardRepository);
        System.out.println("\n---ZMIANA USTAWIEŃ BALANSU DEBETOWEGO---");
        if (paymentCard.isDebtBalanceIsActive())
        {
            paymentCard.setDebtBalanceIsActive(false);
            paymentCard.setDebtBalance(BigDecimal.valueOf(0.0));
            System.out.println("Balans debetowy został dezaktywowany");
        }
        else
        {
            paymentCard.setDebtBalanceIsActive(true);
            System.out.println("Balans debetowy został aktywowany");
            paymentCard.setDebtBalance(prepareDebtBalance(paymentCard.getMaxDebt()));
        }
    }
}
