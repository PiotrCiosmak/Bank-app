package com.ciosmak.bankapp.exception;

public class IllegalOptionSelected extends AbstractException
{
    public IllegalOptionSelected(String errorMessage, String message)
    {
        super(errorMessage, message);
    }
}
