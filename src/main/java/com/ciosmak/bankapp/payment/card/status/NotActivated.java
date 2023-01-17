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

/**
 * This class represents the "NotActivated" status of a payment card in a bank application.
 * The class extends the AbstractPaymentCardService and implements the PaymentCardStatus interface.
 * The class prompts the user to enter a PIN and verifies it against the stored hashed value.
 * If the entered PIN is correct, the status of the payment card is set to Activated and a message is displayed indicating that the card has been activated.
 * If the entered PIN is incorrect, the user is prompted to enter the PIN again until it is correct.
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
public class NotActivated extends AbstractPaymentCardService implements PaymentCardStatus
{
    /**
     * Show the details of the payment card, but since the card is not activated yet, it will activate the card by prompting the user to enter a PIN and verifying it against the stored hashed value.
     * If the entered PIN is correct, the status of the payment card is set to Activated and a message is displayed indicating that the card has been activated.
     * If the entered PIN is incorrect, the user is prompted to enter the PIN again until it is correct.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    @Override
    public void showPaymentCard(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        activatePaymentCard(paymentCardId, paymentCardRepository);
    }

    /**
     * Change the limits of the payment card, but since the card is not activated yet, it will activate the card by prompting the user to enter a PIN and verifying it against the stored hashed value.
     * If the entered PIN is correct, the status of the payment card is set to Activated and a message is displayed indicating that the card has been activated.
     * If the entered PIN is incorrect, the user is prompted to enter the PIN again until it is correct.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    @Override
    public void changeLimits(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        activatePaymentCard(paymentCardId, paymentCardRepository);
    }

    /**
     * Temporarily block the payment card, but since the card is not activated yet, it will activate the card by prompting the user to enter a PIN and verifying it against the stored hashed value.
     * If the entered PIN is correct, the status of the payment card is set to Activated and a message is displayed indicating that the card has been activated.
     * If the entered PIN is incorrect, the user is prompted to enter the PIN again until it is correct.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    @Override
    public void blockTemporarily(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        activatePaymentCard(paymentCardId, paymentCardRepository);
    }

    /**
     * Unlock the payment card, but since the card is not activated yet, it will activate the card by prompting the user to enter a PIN and verifying it against the stored hashed value.
     * If the entered PIN is correct, the status of the payment card is set to Activated and a message is displayed indicating that the card has been activated.
     * If the entered PIN is incorrect, the user is prompted to enter the PIN again until it is correct.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    @Override
    public void unlock(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        activatePaymentCard(paymentCardId, paymentCardRepository);
    }

    /**
     * Unlock the payment card, but since the card is not activated yet, it will activate the card by prompting the user to enter a PIN and verifying it against the stored hashed value.
     * If the entered PIN is correct, the status of the payment card is set to Activated and a message is displayed indicating that the card has been activated.
     * If the entered PIN is incorrect, the user is prompted to enter the PIN again until it is correct.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    @Override
    public void blockPermanently(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        activatePaymentCard(paymentCardId, paymentCardRepository);
    }

    /**
     * Permanently block the payment card, but since the card is not activated yet, it will activate the card by prompting the user to enter a PIN and verifying it against the stored hashed value.
     * If the entered PIN is correct, the status of the payment card is set to Activated and a message is displayed indicating that the card has been activated.
     * If the entered PIN is incorrect, the user is prompted to enter the PIN again until it is correct.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    @Override
    public void changePin(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        activatePaymentCard(paymentCardId, paymentCardRepository);
    }

    /**
     * This method attempts to change the contactless transaction option for the PaymentCard associated with the given PaymentCardId.
     * However, since the PaymentCard is not yet activated, the user will be prompted to activate the card before proceeding with this action.
     *
     * @param paymentCardId         The id of the PaymentCard for which the contactless transaction option should be changed
     * @param paymentCardRepository The repository used to retrieve the PaymentCard associated with the given PaymentCardId
     */
    @Override
    public void changeContactlessTransactionOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        activatePaymentCard(paymentCardId, paymentCardRepository);
    }

    /**
     * This method attempts to change the magnetic strip option for the PaymentCard associated with the given PaymentCardId.
     * However, since the PaymentCard is not yet activated, the user will be prompted to activate the card before proceeding with this action.
     *
     * @param paymentCardId         The id of the PaymentCard for which the magnetic strip option should be changed
     * @param paymentCardRepository The repository used to retrieve the PaymentCard associated with the given PaymentCardId
     */
    @Override
    public void changeMagneticStripOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        activatePaymentCard(paymentCardId, paymentCardRepository);
    }

    /**
     * Change the option for transactions with DDC (Dynamic Currency Conversion) service. Activates card if it's not activated yet.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    @Override
    public void changeTransactionsWithDdcServiceOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        activatePaymentCard(paymentCardId, paymentCardRepository);
    }

    /**
     * Change the option for surcharged transactions. Activates card if it's not activated yet.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    @Override
    public void changeSurchargeTransactionsOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        activatePaymentCard(paymentCardId, paymentCardRepository);
    }

    /**
     * Change the option for debit transactions. Activates card if it's not activated yet.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
    @Override
    public void changeDebitOption(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        activatePaymentCard(paymentCardId, paymentCardRepository);
    }

    /**
     * Activates payment card by prompting the user to enter a PIN and verifying it against the stored hashed value.
     * If the entered PIN is correct, the status of the payment card is set to Activated and a message is displayed indicating that the card has been activated.
     * If the entered PIN is incorrect, the user is prompted to enter the PIN again until it is correct.
     *
     * @param paymentCardId         the id of the payment card
     * @param paymentCardRepository the repository for payment card
     */
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

    /**
     * Pauses the program for a random amount of time to simulate the activation process.
     */
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
