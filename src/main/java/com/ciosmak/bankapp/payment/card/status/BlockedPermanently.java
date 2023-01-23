package com.ciosmak.bankapp.payment.card.status;

import com.ciosmak.bankapp.payment.card.id.PaymentCardId;
import com.ciosmak.bankapp.repository.PaymentCardRepository;
import com.ciosmak.bankapp.service.AbstractPaymentCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class represents the "BlockedPermanently" status of a payment card in a bank application.
 * The class extends the AbstractPaymentCardService and implements the PaymentCardStatus interface.
 * When a card is in this status, it cannot perform any action, all methods will show the message to the user that the card is permanently blocked.
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
public class BlockedPermanently extends AbstractPaymentCardService implements PaymentCardStatus
{
    /**
     * Show the message that the card is permanently blocked, this method will not show any details of the card
     *
     * @param paymentCardId         the id of the payment card that we want to show
     * @param paymentCardRepository the repository of payment cards where we will search for the card with the given id
     */
    @Override
    public void showPaymentCard(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    /**
     * Show the message that the card is permanently blocked, this method will not change any limits of the card
     *
     * @param paymentCardId         the id of the payment card that we want to change the limits
     * @param paymentCardRepository the repository of payment cards where we will search for the card with the given id
     */
    @Override
    public void changeLimits(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    /**
     * Show the message that the card is permanently blocked, this method will not block the card temporarily
     *
     * @param paymentCardId         the id of the payment card that we want to block temporarily
     * @param paymentCardRepository the repository of payment cards where we will search for the card with the given id
     */
    @Override
    public void blockTemporarily(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    /**
     * Show the message that the card is permanently blocked, this method will not unlock the card
     *
     * @param paymentCardId         the id of the payment card that we want to unlock
     * @param paymentCardRepository the repository of payment cards where we will search for the card with the given id
     */
    @Override
    public void unlock(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    /**
     * Show the message that the card is already permanently blocked, this method will not block the card again
     *
     * @param paymentCardId         the id of the payment card that we want to block permanently
     * @param paymentCardRepository the repository of payment cards where we will search for the card with the given id
     */
    @Override
    public void blockPermanently(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    /**
     * Show the message that the card is permanently blocked, this method will not change the PIN of the card
     *
     * @param paymentCardId         the id of the payment card that we want to change the PIN
     * @param paymentCardRepository the repository of payment cards where we will search for the card with the given id
     */
    @Override
    public void changePin(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    /**
     * Show the message that the card is permanently blocked, this method will not change the contactless transaction option of the card
     *
     * @param paymentCardId         the id of the payment card that we want to change the contactless transaction option
     * @param paymentCardRepository the repository of payment cards where we will search for the card with the given id
     */
    @Override
    public void changeContactlessTransactionOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    /**
     * Show the message that the card is permanently blocked, this method will not change the magnetic strip option of the card
     *
     * @param paymentCardId         the id of the payment card that we want to change the magnetic strip option
     * @param paymentCardRepository the repository of payment cards where we will search for the card with the given id
     */
    @Override
    public void changeMagneticStripOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    /**
     * Show the message that the card is permanently blocked, this method will not change the option for transactions with DDC service of the card
     *
     * @param paymentCardId         the id of the payment card that we want to change the option for transactions with DDC service
     * @param paymentCardRepository the repository of payment cards where we will search for the card with the given id
     */
    @Override
    public void changeTransactionsWithDdcServiceOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    /**
     * Show the message that the card is permanently blocked, this method will not change the option for surcharge transactions of the card
     *
     * @param paymentCardId         the id of the payment card that we want to change the option for surcharge transactions
     * @param paymentCardRepository the repository of payment cards where we will search for the card with the given id
     */
    @Override
    public void changeSurchargeTransactionsOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    /**
     * Show the message that the card is permanently blocked, this method will not change the debit option of the card
     *
     * @param paymentCardId         the id of the payment card that we want to change the debit option
     * @param paymentCardRepository the repository of payment cards where we will search for the card with the given id
     */
    @Override
    public void changeDebitOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    /**
     * Show the message that the card is permanently blocked, this method will display a message indicating that the card is permanently blocked and that no action can be performed.
     */
    private void showMessage()
    {
        System.out.println("\n---TA KARTA JEST PERMANENTNIE ZABLKOWANA");
        System.out.println("Brak możliwości przeprowadzenia jakiejkolwiek akcji");
    }
}
