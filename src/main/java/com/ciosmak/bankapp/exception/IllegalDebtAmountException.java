package com.ciosmak.bankapp.exception;

public class IllegalDebtAmountException extends AbstractException
{

    public IllegalDebtAmountException(String errorMessage, String message)
    {
        super(errorMessage, message);
    }
}
