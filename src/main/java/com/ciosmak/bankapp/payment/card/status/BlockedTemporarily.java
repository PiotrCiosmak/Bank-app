package com.ciosmak.bankapp.payment.card.status;

import com.ciosmak.bankapp.entity.PaymentCard;
import com.ciosmak.bankapp.payment.card.id.PaymentCardId;
import com.ciosmak.bankapp.repository.PaymentCardRepository;
import com.ciosmak.bankapp.service.AbstractPaymentCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class represents the "BlockedTemporarily" status of a payment card in a bank application.
 * The class extends the AbstractPaymentCardService and implements the PaymentCardStatus interface.
 * This class defines the behavior and functionality when a payment card is temporarily blocked
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
public class BlockedTemporarily extends AbstractPaymentCardService implements PaymentCardStatus
{
    /**
     * Show the payment card details, but since the card is temporarily blocked so the action can't be performed.
     * It will display a message indicating that the card is temporarily blocked
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    @Override
    public void showPaymentCard(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    /**
     * Change the limits for the payment card, but since the card is temporarily blocked so the action can't be performed.
     * It will display a message indicating that the card is temporarily blocked
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    @Override
    public void changeLimits(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    /**
     * Block the payment card temporarily, but since the card is already temporarily blocked so the action can't be performed.
     * It will display a message indicating that the card is already temporarily blocked
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    @Override
    public void blockTemporarily(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    /**
     * Unlock the payment card and change its status from temporarily blocked to activated.
     * It will display a message indicating that the card has been unlocked.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    @Override
    public void unlock(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        PaymentCard paymentCard = getPaymentCardById(paymentCardId, paymentCardRepository);
        Activated activated = new Activated();
        paymentCard.setStatus(activated);
        System.out.println("\n---KARTA ZOSTAŁA ODBLOKOWANA---");
    }

    /**
     * Block the payment card permanently, changing its status from temporarily blocked to permanently blocked.
     * It will display a message indicating that the card has been blocked permanently.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
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
     * Change the PIN of the payment card, but since the card is temporarily blocked so the action can't be performed.
     * It will display a message indicating that the card is temporarily blocked
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    @Override
    public void changePin(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    /**
     * Change the option for contactless transactions of the payment card, but since the card is temporarily blocked so the action can't be performed.
     * It will display a message indicating that the card is temporarily blocked
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    @Override
    public void changeContactlessTransactionOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    /**
     * Change the option for magnetic strip transactions of the payment card, but since the card is temporarily blocked so the action can't be performed.
     * It will display a message indicating that the card is temporarily blocked
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    @Override
    public void changeMagneticStripOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    /**
     * Change the option for transactions with DDC service of the payment card, but since the card is temporarily blocked so the action can't be performed.
     * It will display a message indicating that the card is temporarily blocked
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    @Override
    public void changeTransactionsWithDdcServiceOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    /**
     * Change the option for surcharge transactions of the payment card, but since the card is temporarily blocked so the action can't be performed.
     * It will display a message indicating that the card is temporarily blocked
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    @Override
    public void changeSurchargeTransactionsOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    /**
     * Change the option for debit transactions of the payment card, but since the card is temporarily blocked so the action can't be performed.
     * It will display a message indicating that the card is temporarily blocked
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    @Override
    public void changeDebitOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        showMessage();
    }

    /**
     * Display a message indicating that the card is temporarily blocked and that any action can't be performed until it's been unlocked.
     */
    private void showMessage()
    {
        System.out.println("\n---KARTA JEST TYMCZASOWO ZABLOKOWANA");
        System.out.println("Aby przeprowadzić jakąkolwiek akcję należy odblokować kartę");
    }
}
