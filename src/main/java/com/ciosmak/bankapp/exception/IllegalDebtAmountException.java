package com.ciosmak.bankapp.exception;

/**
 * Represents an exception for an illegal debt amount.
 *
 * @author Author Piotr Ciosmak
 * @version 1.0
 */
public class IllegalDebtAmountException extends AbstractException
{
    /**
     * Creates a new instance of the exception with the specified error and additional message.
     *
     * @param errorMessage the error message
     * @param message      the additional message
     */
    public IllegalDebtAmountException(String errorMessage, String message)
    {
        super(errorMessage, message);
    }
}
