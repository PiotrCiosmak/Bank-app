package com.ciosmak.bankapp.exception;

public abstract class AbstractException extends Exception
{
    public AbstractException(String errorMessage, String message)
    {
        this.errorMessage = errorMessage;
        this.message = message;
    }

    public void show()
    {
        System.err.print(errorMessage);
        System.err.flush();
        System.out.print(message);
        System.out.flush();
    }

    String errorMessage;
    String message;
}
