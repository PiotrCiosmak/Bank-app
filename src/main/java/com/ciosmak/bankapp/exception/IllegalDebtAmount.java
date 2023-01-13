package com.ciosmak.bankapp.exception;

public class IllegalDebtAmount extends AbstractException
{

    public IllegalDebtAmount(String errorMessage, String message)
    {
        super(errorMessage, message);
    }
}
