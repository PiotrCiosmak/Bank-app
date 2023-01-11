package com.ciosmak.bankapp.menu;

import com.ciosmak.bankapp.bank.account.id.BankAccountId;
import com.ciosmak.bankapp.payment.card.id.PaymentCardId;
import com.ciosmak.bankapp.service.*;
import com.ciosmak.bankapp.user.id.UserId;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu
{
    public static void loginMenu(UserService userService, IdentityDocumentService identityDocumentService, BankAccountService bankAccountService, PaymentCardService paymentCardService)
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
                        identityDocumentService.autoUpdateIdentityDocument(userId);
                        paymentCardService.autoCheckExpiryDate();
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

    public static void mainMenu(UserService userService, PersonalDataService personalDataService, AddressService addressService, IdentityDocumentService identityDocumentService, BankAccountService bankAccountService, PaymentCardService paymentCardService)
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
                    case 1 -> desktop(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService);
                    case 2 -> paymentMenu(userService);
                    case 3 -> products(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService);
                    case 4 -> setting(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService);
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

    public static void desktop(UserService userService, PersonalDataService personalDataService, AddressService addressService, IdentityDocumentService identityDocumentService, BankAccountService bankAccountService, PaymentCardService paymentCardService)
    {
        System.out.println("\n---PULPIT---");
        System.out.println("Łączny balans: " + bankAccountService.getBalanceFromAllBankAccounts(userId));
        System.out.println("Wydatki od początku bieżącego miesiąca: ");//TODO pokazać wydatki ze wszystkich rachunków z danego miesiaca
        System.out.println("Ilość aktywnych rachunków: " + bankAccountService.getNumberOfOpenBankAccounts(userId));
        System.out.println("Ilość aktywnych kart płatniczych: " + paymentCardService.getNumberOfNoPermanentlyBlockedPaymentCards(userId));
        System.out.println("---HISTORIA---");
        //TODO pokazać 5 ostatnich transakcji
        while (true)
        {
            int selectedOption;
            try
            {
                System.out.println("1. Pokaż całą historię");
                System.out.println("2. Wstecz");
                System.out.print("Wybieram: ");
                selectedOption = scanner.nextInt();
                switch (selectedOption)
                {
                    case 1 ->
                    {
                        //TODO przejdź do menu historii
                        return;
                    }
                    case 2 -> mainMenu(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService);
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

    public static void paymentMenu(UserService userService)
    {

    }

    public static void products(UserService userService, PersonalDataService personalDataService, AddressService addressService, IdentityDocumentService identityDocumentService, BankAccountService bankAccountService, PaymentCardService paymentCardService)
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
                        bankAccountMenu(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService);
                        return;
                    }
                    case 3 ->
                    {
                        paymentCardId.setId(paymentCardService.chooseOnePaymentCard(userId));
                        paymentCardMenu(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService);
                        return;
                    }
                    case 4 -> mainMenu(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService);
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

    public static void setting(UserService userService, PersonalDataService personalDataService, AddressService addressService, IdentityDocumentService identityDocumentService, BankAccountService bankAccountService, PaymentCardService paymentCardService)
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
                        addressService.changeAddress(userId);
                        return;
                    }
                    case 3 ->
                    {
                        addressService.changeMailingAddress(userId);
                        return;
                    }
                    case 4 ->
                    {
                        personalDataService.updatePersonalData(userId);
                        return;
                    }
                    case 5 ->
                    {
                        identityDocumentService.updateIdentityDocument(userId, "\n---AKTUALIZACJA DOWÓDU OSOBISTEGO---", "");
                        return;
                    }
                    case 6 -> mainMenu(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService);
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

    private static void bankAccountMenu(UserService userService, PersonalDataService personalDataService, AddressService addressService, IdentityDocumentService identityDocumentService, BankAccountService bankAccountService, PaymentCardService paymentCardService)
    {
        while (true)
        {
            int selectedOption;
            try
            {
                System.out.println("\n---OPCJE RACHUNKU BANKOWEGO---");
                System.out.println("1. Wyświetl");
                System.out.println("2. Zmień nazwę");
                System.out.println("3. Menu główne");
                System.out.print("Wybieram: ");
                selectedOption = scanner.nextInt();

                switch (selectedOption)
                {
                    case 1 -> bankAccountService.showBankAccount(bankAccountId);
                    case 2 -> bankAccountService.changeBankAccountName(bankAccountId);
                    case 3 -> mainMenu(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService);
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

    private static void paymentCardMenu(UserService userService, PersonalDataService personalDataService, AddressService addressService, IdentityDocumentService identityDocumentService, BankAccountService bankAccountService, PaymentCardService paymentCardService)
    {
        while (true)
        {
            int selectedOption;
            try
            {
                System.out.println("\n---OPCJE KARTY PŁATNICZEJ---");
                System.out.println("1. Wyświetl");
                System.out.println("2. Zmień limity");
                System.out.println("3. Odblokuj");
                System.out.println("4. Zablokuj tymczasowo");
                System.out.println("5. Zablokuj pernamentnie");
                System.out.println("6. Więcej");
                System.out.println("7. Menu główne");
                System.out.print("Wybieram: ");
                selectedOption = scanner.nextInt();

                switch (selectedOption)
                {
                    case 1 -> paymentCardService.showPaymentCard(paymentCardId);
                    case 2 -> paymentCardService.changeLimits(paymentCardId);
                    case 3 -> paymentCardService.unlock(paymentCardId);
                    case 4 -> paymentCardService.blockTemporarily(paymentCardId);
                    case 5 -> paymentCardService.blockPermanently(paymentCardId);
                    case 6 -> paymentCardMenuExternal(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService);
                    case 7 -> mainMenu(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService);
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
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do 7.\nSpróbuj ponownie.");
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

    private static void paymentCardMenuExternal(UserService userService, PersonalDataService personalDataService, AddressService addressService, IdentityDocumentService identityDocumentService, BankAccountService bankAccountService, PaymentCardService paymentCardService)
    {
        while (true)
        {
            int selectedOption;
            try
            {
                System.out.println("\n---WIĘCEJ OPCJI KARTY PŁATNICZEJ---");
                System.out.println("1. Zmień pin");
                System.out.println("2. Zmień ustawienia płatności zbliżeniowych");
                System.out.println("3. Zmień ustawienia paska magnetycznego");
                System.out.println("4. Zmień ustawienia usługi DDC");
                System.out.println("5. Zmień ustawienia transakcji z dopłatą");
                System.out.println("6. Zmień ustawienia balansu debetowego");
                System.out.println("7. Powrót");
                System.out.print("Wybieram: ");
                selectedOption = scanner.nextInt();

                switch (selectedOption)
                {
                    case 1 -> paymentCardService.changePin(paymentCardId);
                    case 2 -> paymentCardService.changeContactlessTransactionOption(paymentCardId);
                    case 3 -> paymentCardService.changeMagneticStripOption(paymentCardId);
                    case 4 -> paymentCardService.changeTransactionsWithDdcServiceOption(paymentCardId);
                    case 5 -> paymentCardService.changeSurchargeTransactionsOption(paymentCardId);
                    case 6 -> paymentCardService.changeDebitOption(paymentCardId);
                    case 7 -> paymentCardMenu(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService);
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
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do 7.\nSpróbuj ponownie.");
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

    private static final UserId userId = UserId.getInstance(0L);
    private static final BankAccountId bankAccountId = BankAccountId.getInstance(0L);
    private static final PaymentCardId paymentCardId = PaymentCardId.getInstance(0L);
    private static Scanner scanner = new Scanner(System.in);
}
