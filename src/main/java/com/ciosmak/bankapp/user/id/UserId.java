package com.ciosmak.bankapp.user.id;

/**
 * This class represents a user ID.
 *
 * <p> The class uses the Singleton pattern, where only one instance of the class can be created.
 * This ensures that there is only one unique ID throughout the system. The class can be accessed globally via the
 * {@link #getInstance(Long)} method.
 *
 * <p> The class has methods for setting and getting the ID, and the ID is stored as a private variable.
 * The constructor is private, so the class can only be instantiated within the class.
 *
 * @author Author Piotr Ciosmak
 * @version 1.0
 */
public final class UserId
{
    /**
     * Returns the instance of the class
     *
     * @param id the id of user
     * @return the instance of the class
     */
    public static UserId getInstance(Long id)
    {
        if (instance == null)
        {
            instance = new UserId(id);
        }
        return instance;
    }

    /**
     * Set the id of user
     *
     * @param id the id of user
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * Get the id of user
     *
     * @return the id of user
     */
    public Long getId()
    {
        return this.id;
    }

    /**
     * The id of user
     */
    public Long id;

    /**
     * Constructor is private, so the class can only be instantiated within the class.
     *
     * @param id the id of user
     */
    private UserId(Long id)
    {
        this.id = id;
    }

    /**
     * The instance of the class
     */
    private static UserId instance;
}
