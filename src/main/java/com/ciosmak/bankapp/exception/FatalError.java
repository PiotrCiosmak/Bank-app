package com.ciosmak.bankapp.exception;

public class FatalError
{
    public static void exit()
    {
        System.err.println("BŁĄD KRYTYCZNY!!!");
        System.err.println("OPUSZCZANIE PROGRAMU");
        System.err.flush();
        System.exit(1);
    }
}
