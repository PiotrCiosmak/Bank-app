package com.ciosmak.bankapp.payment.card.status;

import com.ciosmak.bankapp.payment.card.id.PaymentCardId;
import com.ciosmak.bankapp.repository.PaymentCardRepository;
import com.ciosmak.bankapp.service.AbstractPaymentCardService;

/**
 * The PaymentCardStatus interface represents the different statuses that a payment card can have in a bank application.
 * The interface defines methods for different actions that can be performed on a payment card depending on its status.
 * These actions include showing the details of the payment card, changing the limits, blocking and unlocking the card, changing the PIN,
 * changing the contactless transaction option, changing the magnetic strip option, changing the option for transactions with DDC (Dynamic Currency Conversion) service,
 * changing the option for surcharged transactions, and changing the option for debit transactions.
 *
 * @author Author Piotr Ciosmak
 * @version 1.0
 */
public interface PaymentCardStatus
{
    /**
     * Show the details of the payment card, such as its status, limits, and other relevant information.
     * The method may also perform additional actions based on the current status of the card, such as activating the card if it is not yet activated.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    void showPaymentCard(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository);

    /**
     * Change the limits of the payment card, such as the daily withdrawal limit or the maximum transaction limit.
     * The method may also perform additional actions based on the current status of the card, such as activating the card if it is not yet activated.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    void changeLimits(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository);

    /**
     * Temporarily block the payment card to prevent unauthorized transactions.
     * The method may also perform additional actions based on the current status of the card, such as activating the card if it is not yet activated.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    void blockTemporarily(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository);

    /**
     * Unlock the payment card after it has been temporarily blocked.
     * The method may also perform additional actions based on the current status of the card, such as activating the card if it is not yet activated.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    void unlock(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository);

    /**
     * Permanently block the payment card to prevent unauthorized transactions and make the card unusable.
     * The method may also perform additional actions based on the current status of the card, such as activating the card if it is not yet activated.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    void blockPermanently(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository);

    /**
     * Change the personal identification number (PIN) of the payment card.
     * The method may also perform additional actions based on the current status of the card, such as activating the card if it is not yet activated.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    void changePin(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository);

    /**
     * Change the option for contactless transactions on the payment card.
     * The method may also perform additional actions based on the current status of the card, such as activating the card if it is not yet activated.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    void changeContactlessTransactionOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository);

    /**
     * Change the option for transactions using the magnetic strip on the payment card.
     * The method may also perform additional actions based on the current status of the card, such as activating the card if it is not yet activated.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    void changeMagneticStripOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository);

    /**
     * Change the option for transactions with DDC (Dynamic Currency Conversion) service on the payment card.
     * The method may also perform additional actions based on the current status of the card, such as activating the card if it is not yet activated.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    void changeTransactionsWithDdcServiceOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository);

    /**
     * Change the option for surcharged transactions on the payment card.
     * The method may also perform additional actions based on the current status of the card, such as activating the card if it is not yet activated.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    void changeSurchargeTransactionsOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository);

    /**
     * Change the option for debit transactions on the payment card.
     * The method may also perform additional actions based on the current status of the card, such as activating the card if it is not yet activated.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    void changeDebitOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository);
}
