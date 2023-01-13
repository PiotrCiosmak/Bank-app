package com.ciosmak.bankapp.exception;

public class IllegalLimitAmountException extends AbstractException
{
    public IllegalLimitAmountException(String errorMessage, String message)
    {
        super(errorMessage, message);
    }
}
