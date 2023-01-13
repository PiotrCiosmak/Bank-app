package com.ciosmak.bankapp.exception;

public class IllegalLimitAmount extends AbstractException
{
    public IllegalLimitAmount(String errorMessage, String message)
    {
        super(errorMessage, message);
    }
}
