package com.ciosmak.bankapp.payment.card.id;

/**
 * This class represents a payment card ID.
 *
 * <p> The class uses the Singleton pattern, where only one instance of the class can be created.
 * This ensures that there is only one unique ID throughout the system. The class can be accessed globally via the
 * {@link #getInstance(Long)} method.
 *
 * <p> The class has methods for setting and getting the ID, and the ID is stored as a private variable.
 * The constructor is private, so the class can only be instantiated within the class.
 *
 * @author Piotr Ciosmak
 * @version 1.0
 */
public final class PaymentCardId
{
    /**
     * Returns the instance of the class
     *
     * @param id the id of payment card
     * @return the instance of the class
     */
    public static PaymentCardId getInstance(Long id)
    {
        if (instance == null)
        {
            instance = new PaymentCardId(id);
        }
        return instance;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return this.id;
    }

    public Long id;

    /**
     * Constructor is private, so the class can only be instantiated within the class.
     *
     * @param id the id of payment card
     */
    private PaymentCardId(Long id)
    {
        this.id = id;
    }

    private static PaymentCardId instance;
}
