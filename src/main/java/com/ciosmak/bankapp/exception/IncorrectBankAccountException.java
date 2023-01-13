package com.ciosmak.bankapp.exception;

public class IncorrectBankAccountException extends AbstractException
{
    public IncorrectBankAccountException(String errorMessage, String message)
    {
        super(errorMessage, message);
    }
}
