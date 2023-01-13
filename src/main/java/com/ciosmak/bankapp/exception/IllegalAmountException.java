package com.ciosmak.bankapp.exception;

public class IllegalAmountException extends AbstractException
{
    public IllegalAmountException(String errorMessage, String message)
    {
        super(errorMessage, message);
    }
}
