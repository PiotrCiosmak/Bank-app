package com.ciosmak.bankapp.menu;

import com.ciosmak.bankapp.service.UserService;
import com.ciosmak.bankapp.user.id.UserId;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu
{
    public static void loginMenu(UserService userService, UserId userId)
    {
        while (true)
        {
            int selectedOption;
            try
            {
                System.out.println("\n---MENU LOGOWANIA---");
                System.out.println("1. Logowanie");
                System.out.println("2. Rejestracja");
                System.out.println("3. Wyjście");
                System.out.print("Wybieram: ");
                selectedOption = scanner.nextInt();

                switch (selectedOption)
                {
                    case 1 ->
                    {
                        userService.signIn(userId);
                        return;
                    }
                    case 2 ->
                    {
                        userService.register(userId);
                        return;
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

    public static void mainMenu(UserService userService, UserId userId)
    {
        while (true)
        {
            int selectedOption;
            try
            {
                System.out.println("\n---MENU GŁÓWNE---");
                System.out.println("1. Pulpit");
                System.out.println("2. Menu płatności");
                System.out.println("3. Produkty");
                System.out.println("4. Ustawienia");
                System.out.println("5. Wyjście");
                System.out.print("Wybieram: ");
                selectedOption = scanner.nextInt();

                switch (selectedOption)
                {
                    case 1 -> desktop(userService, userId);
                    case 2 -> paymentMenu(userService, userId);
                    case 3 -> products(userService, userId);
                    case 4 -> setting(userService, userId);
                    case 5 -> System.exit(0);
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
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do 5.\nSpróbuj ponownie.");
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

    public static void desktop(UserService userService, UserId userId)
    {

    }

    public static void paymentMenu(UserService userService, UserId userId)
    {

    }

    public static void products(UserService userService, UserId userId)
    {

    }

    public static void setting(UserService userService, UserId userId)
    {
        while (true)
        {
            int selectedOption;
            try
            {
                System.out.println("\n---USTAWIENIA---");
                System.out.println("1. Zmień adres");
                System.out.println("2. Zmień adres korespondencyjny");
                System.out.println("3. Aktualizuj dane osobowe");
                System.out.println("4. Aktualizuj dowód osobisty");
                System.out.println("5. Wstecz");
                System.out.print("Wybieram: ");
                selectedOption = scanner.nextInt();

                switch (selectedOption)
                {
                    case 1 ->
                    {
                        userService.changeAddress(userId);
                        return;
                    }
                    case 2 ->
                    {
                        userService.changeMailingAddress(userId);
                        return;
                    }
                    case 3 ->
                    {
                        userService.updatePersonalData(userId);
                        return;
                    }
                    case 4 ->
                    {
                        userService.updateIdentityDocument(userId);
                        return;
                    }
                    case 5 -> mainMenu(userService, userId);
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
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do 5.\nSpróbuj ponownie.");
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
