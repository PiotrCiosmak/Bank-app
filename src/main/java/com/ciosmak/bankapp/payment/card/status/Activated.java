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

/**
 * Service class representing the "Activated" state of the PaymentCardStatus enum.
 * Provide implementation for the showPaymentCard and changeLimits methods defined in PaymentCardStatus interface.
 *
 * @author Piotr Ciosmak
 * @version 1.0
 * @see AbstractPaymentCardService
 * @see PaymentCardStatus
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class Activated extends AbstractPaymentCardService implements PaymentCardStatus
{
    /**
     * Displays the details of the PaymentCard object
     *
     * @param paymentCardId         the id of the PaymentCard object to be displayed
     * @param paymentCardRepository the repository used to retrieve the PaymentCard object
     */
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

    /**
     * Changes the limits (e.g. payment limit per day, withdraw limit per day) of a PaymentCard object.
     *
     * @param paymentCardId         the id of the PaymentCard object to be changed
     * @param paymentCardRepository the repository used to retrieve the PaymentCard object
     */
    @Override
    public void changeLimits(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        PaymentCard paymentCard = getPaymentCardById(paymentCardId, paymentCardRepository);
        System.out.println("\n---ZMIANA USTAWIEŃ LIMITÓW---");
        paymentCard.setPaymentLimitPerDay(setLimitPerDay("Podaj nowy dzienny limit płatności: "));
        paymentCard.setWithdrawLimitPerDay(setLimitPerDay("Podaj nowy dzienny limit wypłat z bankomatów: "));
        paymentCard.setInternetTransactionLimitPerDay(setLimitPerDay("Podaj nowy dzienny limit płatności w internecie: "));
    }

    /**
     * Blocks a PaymentCard temporarily.
     *
     * @param paymentCardId         The id of the PaymentCard object to be blocked.
     * @param paymentCardRepository The repository used to retrieve the PaymentCard object.
     */
    @Override
    public void blockTemporarily(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        PaymentCard paymentCard = getPaymentCardById(paymentCardId, paymentCardRepository);
        BlockedTemporarily blockedTemporarily = new BlockedTemporarily();
        paymentCard.setStatus(blockedTemporarily);
        System.out.println("\n---KARTA ZOSTAŁA ZABLOKOWANA TYMCZASOWO---");
    }

    /**
     * Unlocks a PaymentCard if it is temporarily blocked.
     *
     * @param paymentCardId         The id of the PaymentCard object to be unlocked.
     * @param paymentCardRepository The repository used to retrieve the PaymentCard object.
     */
    @Override
    public void unlock(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        System.out.println("\n---KARTA NIE JEST ZABLOKOWANA---");
    }

    /**
     * Blocks a PaymentCard permanently.
     *
     * @param paymentCardId         The id of the PaymentCard object to be blocked.
     * @param paymentCardRepository The repository used to retrieve the PaymentCard object.
     */
    @Override
    public void blockPermanently(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        PaymentCard paymentCard = getPaymentCardById(paymentCardId, paymentCardRepository);
        BlockedPermanently blockedPermanently = new BlockedPermanently();
        paymentCard.setStatus(blockedPermanently);
        System.out.println("\n---KARTA ZOSTAŁA ZABLKOWANA PERNAMETNIE---");
    }

    /**
     * Changes the PIN of a PaymentCard.
     *
     * @param paymentCardId         The id of the PaymentCard object whose PIN will be changed.
     * @param paymentCardRepository The repository used to retrieve the PaymentCard object.
     */
    @Override
    public void changePin(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        PaymentCard paymentCard = getPaymentCardById(paymentCardId, paymentCardRepository);
        System.out.println("\n---ZMIANA PINU---");
        paymentCard.setPin(preparePin());
    }

    /**
     * Method used to change the contactless transaction option for a specific payment card.
     * If contactless transactions are currently enabled for the given card, they will be disabled, and vice versa.
     *
     * @param paymentCardId         the id of the payment card for which we want to change the contactless transaction option
     * @param paymentCardRepository the repository of payment cards where we will search for the card with the given id
     */
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

    /**
     * Method used to change the magnetic strip option for a specific payment card.
     * If magnetic strip is currently enabled for the given card, it will be disabled, and vice versa.
     *
     * @param paymentCardId         the id of the payment card for which we want to change the magnetic strip option
     * @param paymentCardRepository the repository of payment cards where we will search for the card with the given id
     */
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

    /**
     * Method used to change the option for transactions with DDC service for a specific payment card.
     * If transactions with DDC service are currently enabled for the given card, they will be disabled, and vice versa.
     *
     * @param paymentCardId         the id of the payment card for which we want to change the option for transactions with DDC service
     * @param paymentCardRepository the repository of payment cards where we will search for the card with the given id
     */
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

    /**
     * Method used to change the option for surcharge transactions for a specific payment card.
     * If surcharge transactions are currently enabled for the given card, they will be disabled, and vice versa.
     *
     * @param paymentCardId         the id of the payment card for which we want to change the option for surcharge transactions
     * @param paymentCardRepository the repository of payment cards where we will search for the card with the given id
     */
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

    /**
     * Method used to change the option for debit balance for a specific payment card.
     * If debit balance is currently enabled for the given card, it will be disabled and set to zero, and vice versa.
     *
     * @param paymentCardId         the id of the payment card for which we want to change the option for debit balance
     * @param paymentCardRepository the repository of payment cards where we will search for the card with the given id
     */
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
