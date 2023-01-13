package com.ciosmak.bankapp.exception;

public class IllegalOptionSelectedException extends AbstractException
{
    public IllegalOptionSelectedException(String errorMessage, String message)
    {
        super(errorMessage, message);
    }
}
