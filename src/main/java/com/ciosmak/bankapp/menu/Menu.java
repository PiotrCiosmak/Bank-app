package com.ciosmak.bankapp.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu
{
    public static String mainLoginMenu()
    {
        while (true)
        {
            int selectedOption;
            try
            {
                System.out.println("1. Logowanie");
                System.out.println("2. Rejestracja");
                System.out.println("3. Wyjście");
                System.out.print("Wybieram: ");
                selectedOption = scanner.nextInt();


                switch (selectedOption)
                {
                    case 1 ->
                    {
                        return "sign_in";
                    }
                    case 2 ->
                    {
                        return "register";
                    }
                    case 3 -> System.exit(0);
                    default ->
                    {
                        System.err.println("Nie ma takiej opcji.\nSpróbuj ponownie.");
                        System.err.flush();
                    }
                }
            }
            catch (InputMismatchException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do 3.\nSpróbuj ponownie.");
                System.err.flush();
            }
            catch (Exception e)
            {
                System.err.println("BŁĄD KRYTYCZNY!!!");
                System.err.println("OPUSZCZANIE PROGRAMU");
                System.err.flush();
                System.exit(1);
            }
        }
    }

    private static Scanner scanner = new Scanner(System.in);
}
