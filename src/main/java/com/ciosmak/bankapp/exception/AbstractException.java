package com.ciosmak.bankapp.exception;

/**
 * Abstract class for creating custom exceptions.
 *
 * @author Author Piotr Ciosmak
 * @version 1.0
 */
public abstract class AbstractException extends Exception
{
    /**
     * Creates a new instance of the exception with the specified error and additional message.
     *
     * @param errorMessage the error message
     * @param message      the additional message
     */
    public AbstractException(String errorMessage, String message)
    {
        this.errorMessage = errorMessage;
        this.message = message;
    }

    /**
     * Prints the error message to the error output stream and the additional message to the standard output stream.
     */
    public void show()
    {
        System.err.print(errorMessage);
        System.err.flush();
        System.out.print(message);
        System.out.flush();
    }

    /**
     * The error message to be displayed.
     */
    protected String errorMessage;

    /**
     * The additional message to be displayed.
     */
    protected String message;
}
