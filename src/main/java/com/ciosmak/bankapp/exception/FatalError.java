package com.ciosmak.bankapp.exception;

/**
 * Represents a fatal error in the program.
 *
 * @author Author Piotr Ciosmak
 * @version 1.0
 */
public class FatalError
{
    /**
     * Prints an error message and exits the program with an exit status of 1.
     */
    public static void exit()
    {
        System.err.println("BŁĄD KRYTYCZNY!!!");
        System.err.println("OPUSZCZANIE PROGRAMU");
        System.err.flush();
        System.exit(1);
    }
}
