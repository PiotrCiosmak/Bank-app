package com.ciosmak.bankapp.exception;

public class InvalidDateException extends AbstractException
{
    public InvalidDateException(String errorMessage, String message)
    {
        super(errorMessage, message);
    }
}
