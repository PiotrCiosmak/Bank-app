package com.ciosmak.bankapp.exception;

/**
 * Represents an exception for an illegal answer to a true/false question.
 *
 * @author Piotr Ciosmak
 * @version 1.0
 */
public class IllegalAnswerTrueFalseException extends AbstractException
{
    /**
     * Creates a new instance of the exception with the specified error and additional message.
     *
     * @param errorMessage the error message
     * @param message      the additional message
     */
    public IllegalAnswerTrueFalseException(String errorMessage, String message)
    {
        super(errorMessage, message);
    }
}
