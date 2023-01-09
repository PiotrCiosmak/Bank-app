package com.ciosmak.bankapp.menu;

import com.ciosmak.bankapp.bank.account.id.BankAccountId;
import com.ciosmak.bankapp.payment.card.id.PaymentAccountId;
import com.ciosmak.bankapp.service.BankAccountService;
import com.ciosmak.bankapp.service.PaymentCardService;
import com.ciosmak.bankapp.service.UserService;
import com.ciosmak.bankapp.user.id.UserId;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu
{
    public static void loginMenu(UserService userService, BankAccountService bankAccountService)
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
                        userService.autoUpdateIdentityDocument(userId);
                        return;
                    }
                    case 2 ->
                    {
                        userService.register(userId);
                        bankAccountService.createBankAccount(userId);
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

    public static void mainMenu(UserService userService, BankAccountService bankAccountService, PaymentCardService paymentCardService)
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
                    case 1 -> desktop(userService);
                    case 2 -> paymentMenu(userService);
                    case 3 -> products(userService, bankAccountService, paymentCardService);
                    case 4 -> setting(userService, bankAccountService, paymentCardService);
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

    public static void desktop(UserService userService)
    {

    }

    public static void paymentMenu(UserService userService)
    {

    }

    public static void products(UserService userService, BankAccountService bankAccountService, PaymentCardService paymentCardService)
    {
        while (true)
        {
            int selectedOption;
            try
            {
                System.out.println("\n---PRODUKTY---");
                System.out.println("1. Stwórz nowy rachunek");
                System.out.println("2. Wybierz rachunek");
                System.out.println("3. Wybierz kartę płatniczą");
                System.out.println("4. Wstecz");
                System.out.print("Wybieram: ");
                selectedOption = scanner.nextInt();

                switch (selectedOption)
                {
                    case 1 ->
                    {
                        bankAccountService.createBankAccount(userId);
                        return;
                    }
                    case 2 ->
                    {
                        bankAccountId.setId(bankAccountService.chooseOneBankAccount(userId));
                        bankAccountMenu(bankAccountService);
                        return;
                    }
                    case 3 ->
                    {
                        paymentCardService.chooseOnePaymentCard(userId);
                        return;
                    }
                    case 4 -> mainMenu(userService, bankAccountService, paymentCardService);
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
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do 4.\nSpróbuj ponownie.");
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

    public static void setting(UserService userService, BankAccountService bankAccountService, PaymentCardService paymentCardService)
    {
        while (true)
        {
            int selectedOption;
            try
            {
                System.out.println("\n---USTAWIENIA---");
                System.out.println("1. Zmień hasło");
                System.out.println("2. Zmień adres");
                System.out.println("3. Zmień adres korespondencyjny");
                System.out.println("4. Aktualizuj dane osobowe");
                System.out.println("5. Aktualizuj dowód osobisty");
                System.out.println("6. Wstecz");
                System.out.print("Wybieram: ");
                selectedOption = scanner.nextInt();

                switch (selectedOption)
                {
                    case 1 ->
                    {
                        userService.changePassword(userId);
                        return;
                    }
                    case 2 ->
                    {
                        userService.changeAddress(userId);
                        return;
                    }
                    case 3 ->
                    {
                        userService.changeMailingAddress(userId);
                        return;
                    }
                    case 4 ->
                    {
                        userService.updatePersonalData(userId);
                        return;
                    }
                    case 5 ->
                    {
                        userService.updateIdentityDocument(userId, "\n---AKTUALIZACJA DOWÓDU OSOBISTEGO---", "");
                        return;
                    }
                    case 6 -> mainMenu(userService, bankAccountService, paymentCardService);
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
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do 6.\nSpróbuj ponownie.");
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

    private static void bankAccountMenu(BankAccountService bankAccountService)
    {
        while (true)
        {
            int selectedOption;
            try
            {
                System.out.println("\n---OPCJE RACHUNKU BANKOWEGO---");
                System.out.println("1. Wyświetl");
                System.out.println("2. Zmień nazwę");
                System.out.print("Wybieram: ");
                selectedOption = scanner.nextInt();

                switch (selectedOption)
                {
                    case 1 ->
                    {
                        bankAccountService.showBankAccount(bankAccountId);
                        return;
                    }
                    case 2 ->
                    {
                        bankAccountService.changeBankAccountName(bankAccountId);
                        return;
                    }
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
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do 2.\nSpróbuj ponownie.");
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

    private static UserId userId = UserId.getInstance(0L);
    private static BankAccountId bankAccountId = BankAccountId.getInstance(0L);
    private static PaymentAccountId paymentAccountId = PaymentAccountId.getInstance(0L);

    private static Scanner scanner = new Scanner(System.in);
}
