package com.ciosmak.bankapp.menu;

import com.ciosmak.bankapp.bank.account.id.BankAccountId;
import com.ciosmak.bankapp.exception.FatalError;
import com.ciosmak.bankapp.exception.IllegalOptionSelectedException;
import com.ciosmak.bankapp.payment.card.id.PaymentCardId;
import com.ciosmak.bankapp.service.*;
import com.ciosmak.bankapp.user.id.UserId;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Menu class is responsible for displaying menus and handling user input in the bank application.
 * It provides methods for displaying the login menu, main menu and other menus such as payment menu, products, settings.
 * It also provides methods for handling exceptions and calling services for performing actions on data.
 *
 * @author Piotr Ciosmak
 * @version 1.0
 */
public class Menu
{
    /**
     * Method to display the login menu and handle user input.
     * It also calls the transfer service to auto make transfers.
     *
     * @param userService             User service.
     * @param identityDocumentService Identity document service.
     * @param bankAccountService      Bank account service.
     * @param paymentCardService      Payment card service.
     * @param transferService         Transfer service.
     */
    public static void loginMenu(UserService userService, IdentityDocumentService identityDocumentService, BankAccountService bankAccountService, PaymentCardService paymentCardService, TransferService transferService)
    {
        transferService.autoMakeTransfers();
        int selectedOption;
        while (true)
        {
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
                    default -> throw new IllegalOptionSelectedException("Nie ma takiej opcji.\nSpróbuj ponownie.\n", "");
                }
            }
            catch (InputMismatchException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do 3.\nSpróbuj ponownie.");
                System.err.flush();
            }
            catch (IllegalOptionSelectedException e)
            {
                e.show();
            }
            catch (Exception e)
            {
                FatalError.exit();
            }
        }
    }

    /**
     * The mainMenu method provides the main menu of the application. It allows the user to navigate through the different functionalities of the application by selecting an option from the menu.
     *
     * @param userService             instance of UserService class which allow to perform user operations
     * @param personalDataService     instance of PersonalDataService class which allow to perform operations on user personal data
     * @param addressService          instance of AddressService class which allow to perform operations on user address
     * @param identityDocumentService instance of IdentityDocumentService class which allow to perform operations on user identity document
     * @param bankAccountService      instance of BankAccountService class which allow to perform operations on user bank accounts
     * @param paymentCardService      instance of PaymentCardService class which allow to perform operations on user payment cards
     * @param transferService         instance of TransferService class which allow to perform operations on user transfers
     * @param historyService          instance of HistoryService class which allow to perform operations on user history
     */
    public static void mainMenu(UserService userService, PersonalDataService personalDataService, AddressService addressService, IdentityDocumentService identityDocumentService, BankAccountService bankAccountService, PaymentCardService paymentCardService, TransferService transferService, HistoryService historyService)
    {
        int selectedOption;
        while (true)
        {
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
                    case 1 -> desktop(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService, transferService, historyService);
                    case 2 -> paymentMenu(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService, transferService, historyService);
                    case 3 -> products(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService, transferService, historyService);
                    case 4 -> setting(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService, transferService, historyService);
                    case 5 -> System.exit(0);
                    default -> throw new IllegalOptionSelectedException("Nie ma takiej opcji.\nSpróbuj ponownie.\n", "");
                }
            }
            catch (InputMismatchException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do 5.\nSpróbuj ponownie.");
                System.err.flush();
            }
            catch (IllegalOptionSelectedException e)
            {
                e.show();
            }
            catch (Exception e)
            {
                FatalError.exit();
            }
        }
    }

    /**
     * The desktop method provides the user with a summary of their financial information, including their total balance across all bank accounts, their expenses and income for the current month, the number of active bank accounts and payment cards, and the last five transactions from their history. The user also has the option to view their full transaction history.
     *
     * @param userService             instance of UserService class which allow to perform user operations
     * @param personalDataService     instance of PersonalDataService class which allow to perform operations on user personal data
     * @param addressService          instance of AddressService class which allow to perform operations on user address
     * @param identityDocumentService instance of IdentityDocumentService class which allow to perform operations on user identity document
     * @param bankAccountService      instance of BankAccountService class which allow to perform operations on user bank accounts
     * @param paymentCardService      instance of PaymentCardService class which allow to perform operations on user payment cards
     * @param transferService         instance of TransferService class which allow to perform operations on user transfers
     * @param historyService          instance of HistoryService class which allow to perform operations on user history
     */
    public static void desktop(UserService userService, PersonalDataService personalDataService, AddressService addressService, IdentityDocumentService identityDocumentService, BankAccountService bankAccountService, PaymentCardService paymentCardService, TransferService transferService, HistoryService historyService)
    {
        System.out.println("\n---PULPIT---");
        System.out.println("Łączny balans: " + bankAccountService.getBalanceFromAllBankAccounts(userId));
        System.out.println("Wydatki od początku bieżącego miesiąca: " + historyService.getExpensesForCurrentMonth(userId) + "zł");
        System.out.println("Przychody od początku bieżącego miesiąca: " + historyService.getIncomeForCurrentMonth(userId) + "zł");
        System.out.println("Ilość aktywnych rachunków: " + bankAccountService.getNumberOfOpenBankAccounts(userId));
        System.out.println("Ilość aktywnych kart płatniczych: " + paymentCardService.getNumberOfNoPermanentlyBlockedPaymentCards(userId));
        System.out.println("---NAJNOWSZA HISTORIA---");
        historyService.showLastFiveTransactions(userId);
        int selectedOption;
        while (true)
        {
            try
            {
                System.out.println("\n---HISTORIA---");
                System.out.println("1. Pokaż całą historię");
                System.out.println("2. Wstecz");
                System.out.print("Wybieram: ");
                selectedOption = scanner.nextInt();
                switch (selectedOption)
                {
                    case 1 ->
                    {
                        historyService.showHistory(userId);
                        return;
                    }
                    case 2 -> mainMenu(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService, transferService, historyService);
                    default -> throw new IllegalOptionSelectedException("Nie ma takiej opcji.\nSpróbuj ponownie.\n", "");
                }
            }
            catch (InputMismatchException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do 2.\nSpróbuj ponownie.");
                System.err.flush();
            }
            catch (IllegalOptionSelectedException e)
            {
                e.show();
            }
            catch (Exception e)
            {
                FatalError.exit();
            }
        }
    }

    /**
     * The paymentMenu method provides the user with options for managing their payments, including creating new transfers and viewing their transaction history.
     *
     * @param userService             instance of UserService class which allow to perform user operations
     * @param personalDataService     instance of PersonalDataService class which allow to perform operations on user personal data
     * @param addressService          instance of AddressService class which allow to perform operations on user address
     * @param identityDocumentService instance of IdentityDocumentService class which allow to perform operations on user identity document
     * @param bankAccountService      instance of BankAccountService class which allow to perform operations on user bank accounts
     * @param paymentCardService      instance of PaymentCardService class which allow to perform operations on user payment cards
     * @param transferService         instance of TransferService class which allow to perform operations on user transfers
     * @param historyService          instance of HistoryService class which allow to perform operations on user history
     */
    public static void paymentMenu(UserService userService, PersonalDataService personalDataService, AddressService addressService, IdentityDocumentService identityDocumentService, BankAccountService bankAccountService, PaymentCardService paymentCardService, TransferService transferService, HistoryService historyService)
    {
        int selectedOption;
        while (true)
        {
            try
            {
                System.out.println("\n---PŁATNOŚCI---");
                System.out.println("1. Nowy przelew");
                System.out.println("2. Historia");
                System.out.println("3. Wstecz");
                System.out.print("Wybieram: ");
                selectedOption = scanner.nextInt();

                switch (selectedOption)
                {
                    case 1 -> transferService.create(userId);
                    case 2 -> historyService.showHistory(userId);
                    case 3 -> mainMenu(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService, transferService, historyService);
                    default -> throw new IllegalOptionSelectedException("Nie ma takiej opcji.\nSpróbuj ponownie.\n", "");
                }
            }
            catch (InputMismatchException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do 4.\nSpróbuj ponownie.");
                System.err.flush();
            }
            catch (IllegalOptionSelectedException e)
            {
                e.show();
            }
            catch (Exception e)
            {
                FatalError.exit();
            }
        }
    }

    /**
     * The products method provides a menu for creating and selecting bank accounts and payment cards. The user is prompted to select one of the following options:
     * Create a new bank account
     * Select a bank account
     * Select a payment card
     * Go back to the main menu
     * The method takes in several service objects as parameters, including a UserService, PersonalDataService, AddressService, IdentityDocumentService, BankAccountService, PaymentCardService, TransferService, and HistoryService. These services are used to perform various actions related to creating and selecting bank accounts and payment cards.
     * The method uses a while loop to continuously prompt the user to select an option until the user chooses to go back to the main menu. If an invalid option is selected, an error message is displayed and the user is prompted to try again.
     *
     * @param userService             User service object used to perform actions related to users.
     * @param personalDataService     Personal data service object used to perform actions related to personal data.
     * @param addressService          Address service object used to perform actions related to addresses.
     * @param identityDocumentService Identity document service object used to perform actions related to identity documents.
     * @param bankAccountService      Bank account service object used to perform actions related to bank accounts.
     * @param paymentCardService      Payment card service object used to perform actions related to payment cards.
     * @param transferService         Transfer service object used to perform actions related to transfers.
     * @param historyService          History service object used to perform actions related to history.
     */
    public static void products(UserService userService, PersonalDataService personalDataService, AddressService addressService, IdentityDocumentService identityDocumentService, BankAccountService bankAccountService, PaymentCardService paymentCardService, TransferService transferService, HistoryService historyService)
    {
        int selectedOption;
        while (true)
        {
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
                        bankAccountMenu(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService, transferService, historyService);
                        return;
                    }
                    case 3 ->
                    {
                        paymentCardId.setId(paymentCardService.chooseOnePaymentCard(userId));
                        paymentCardMenu(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService, transferService, historyService);
                        return;
                    }
                    case 4 -> mainMenu(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService, transferService, historyService);
                    default -> throw new IllegalOptionSelectedException("Nie ma takiej opcji.\nSpróbuj ponownie.\n", "");
                }
            }
            catch (InputMismatchException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do 4.\nSpróbuj ponownie.");
                System.err.flush();
            }
            catch (IllegalOptionSelectedException e)
            {
                e.show();
            }
            catch (Exception e)
            {
                FatalError.exit();
            }
        }
    }

    /**
     * The setting method provides a menu for updating user information such as password, address, personal data, and identity documents. The user is prompted to select one of the following options:
     * Change password
     * Change address
     * Change mailing address
     * Update personal data
     * Update identity document
     * Go back to the main menu
     * The method takes in several service objects as parameters, including a UserService, PersonalDataService, AddressService, IdentityDocumentService, BankAccountService, PaymentCardService, TransferService, and HistoryService. These services are used to perform various actions related to updating user information.
     * The method uses a while loop to continuously prompt the user to select an option until the user chooses to go back to the main menu. If an invalid option is selected, an error message is displayed and the user is prompted to try again.
     *
     * @param userService             User service object used to perform actions related to users.
     * @param personalDataService     Personal data service object used to perform actions related to personal data.
     * @param addressService          Address service object used to perform actions related to addresses.
     * @param identityDocumentService Identity document service object used to perform actions related to identity documents.
     * @param bankAccountService      Bank account service object used to perform actions related to bank accounts.
     * @param paymentCardService      Payment card service object used to perform actions related to payment cards.
     * @param transferService         Transfer service object used to perform actions related to transfers.
     * @param historyService          History service object used to perform actions related to history.
     */
    public static void setting(UserService userService, PersonalDataService personalDataService, AddressService addressService, IdentityDocumentService identityDocumentService, BankAccountService bankAccountService, PaymentCardService paymentCardService, TransferService transferService, HistoryService historyService)
    {
        int selectedOption;
        while (true)
        {
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
                    case 6 -> mainMenu(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService, transferService, historyService);
                    default -> throw new IllegalOptionSelectedException("Nie ma takiej opcji.\nSpróbuj ponownie.\n", "");
                }
            }
            catch (InputMismatchException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do 6.\nSpróbuj ponownie.");
                System.err.flush();
            }
            catch (IllegalOptionSelectedException e)
            {
                e.show();
            }
            catch (Exception e)
            {
                FatalError.exit();
            }
        }
    }

    /**
     * The bankAccountMenu method provides a menu for viewing and updating information about a specific bank account. The user is prompted to select one of the following options:
     * Show the bank account details
     * Change the bank account name
     * Go back to the main menu
     * The method takes in several service objects as parameters, including a UserService, PersonalDataService, AddressService, IdentityDocumentService, BankAccountService, PaymentCardService, TransferService, and HistoryService. These services are used to perform various actions related to the bank account.
     * The method uses a while loop to continuously prompt the user to select an option until the user chooses to go back to the main menu. If an invalid option is selected, an error message is displayed and the user is prompted to try again.
     *
     * @param userService             User service object used to perform actions related to users.
     * @param personalDataService     Personal data service object used to perform actions related to personal data.
     * @param addressService          Address service object used to perform actions related to addresses.
     * @param identityDocumentService Identity document service object used to perform actions related to identity documents.
     * @param bankAccountService      Bank account service object used to perform actions related to bank accounts.
     * @param paymentCardService      Payment card service object used to perform actions related to payment cards.
     * @param transferService         Transfer service object used to perform actions related to transfers.
     * @param historyService          History service object used to perform actions related to history.
     */
    private static void bankAccountMenu(UserService userService, PersonalDataService personalDataService, AddressService addressService, IdentityDocumentService identityDocumentService, BankAccountService bankAccountService, PaymentCardService paymentCardService, TransferService transferService, HistoryService historyService)
    {
        int selectedOption;
        while (true)
        {
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
                    case 3 -> mainMenu(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService, transferService, historyService);
                    default -> throw new IllegalOptionSelectedException("Nie ma takiej opcji.\nSpróbuj ponownie.\n", "");
                }
            }
            catch (InputMismatchException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do 3.\nSpróbuj ponownie.");
                System.err.flush();
            }
            catch (IllegalOptionSelectedException e)
            {
                e.show();
            }
            catch (Exception e)
            {
                FatalError.exit();
            }
        }
    }

    /**
     * The paymentCardMenu method provides a menu for viewing and managing information about a specific payment card. The user is prompted to select one of the following options:
     * Show the payment card details
     * Change the payment card limits
     * Unlock the payment card
     * Block the payment card temporarily
     * Block the payment card permanently
     * Additional options
     * Go back to the main menu
     * The method takes in several service objects as parameters, including a UserService, PersonalDataService, AddressService, IdentityDocumentService, BankAccountService, PaymentCardService, TransferService, and HistoryService. These services are used to perform various actions related to the payment card.
     * The method uses a while loop to continuously prompt the user to select an option until the user chooses to go back to the main menu. If an invalid option is selected, an error message is displayed and the user is prompted to try again.
     *
     * @param userService             User service object used to perform actions related to users.
     * @param personalDataService     Personal data service object used to perform actions related to personal data.
     * @param addressService          Address service object used to perform actions related to addresses.
     * @param identityDocumentService Identity document service object used to perform actions related to identity documents.
     * @param bankAccountService      Bank account service object used to perform actions related to bank accounts.
     * @param paymentCardService      Payment card service object used to perform actions related to payment cards.
     * @param transferService         Transfer service object used to perform actions related to transfers.
     * @param historyService          History service object used to perform actions related to history.
     */
    private static void paymentCardMenu(UserService userService, PersonalDataService personalDataService, AddressService addressService, IdentityDocumentService identityDocumentService, BankAccountService bankAccountService, PaymentCardService paymentCardService, TransferService transferService, HistoryService historyService)
    {
        int selectedOption;
        while (true)
        {
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
                    case 6 -> paymentCardMenuExternal(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService, transferService, historyService);
                    case 7 -> mainMenu(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService, transferService, historyService);
                    default -> throw new IllegalOptionSelectedException("Nie ma takiej opcji.\nSpróbuj ponownie.\n", "");
                }
            }
            catch (InputMismatchException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do 7.\nSpróbuj ponownie.");
                System.err.flush();
            }
            catch (IllegalOptionSelectedException e)
            {
                e.show();
            }
            catch (Exception e)
            {
                FatalError.exit();
            }
        }
    }

    /**
     * The method paymentCardMenuExternal is used to display and handle options related to a payment card.
     * It allows the user to change various settings such as pin, contactless transaction options, magnetic strip options,
     * DDC service options, surcharge transaction options and debit options.
     *
     * @param userService             an object that allows to perform operations on user data
     * @param personalDataService     an object that allows to perform operations on personal data
     * @param addressService          an object that allows to perform operations on address data
     * @param identityDocumentService an object that allows to perform operations on identity document data
     * @param bankAccountService      an object that allows to perform operations on bank account data
     * @param paymentCardService      an object that allows to perform operations on payment card data
     * @param transferService         an object that allows to perform transfer operations
     * @param historyService          an object that allows to display history of operations
     */
    private static void paymentCardMenuExternal(UserService userService, PersonalDataService personalDataService, AddressService addressService, IdentityDocumentService identityDocumentService, BankAccountService bankAccountService, PaymentCardService paymentCardService, TransferService transferService, HistoryService historyService)
    {
        int selectedOption;
        while (true)
        {
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
                    case 7 -> paymentCardMenu(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService, transferService, historyService);
                    default -> throw new IllegalOptionSelectedException("Nie ma takiej opcji.\nSpróbuj ponownie.\n", "");
                }
            }
            catch (InputMismatchException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do 7.\nSpróbuj ponownie.");
                System.err.flush();
            }
            catch (IllegalOptionSelectedException e)
            {
                e.show();
            }
            catch (Exception e)
            {
                FatalError.exit();
            }
        }
    }

    /**
     * userId is a static final variable of UserId type. It is used to store an instance of UserId with an initial value of 0L.
     */
    private static final UserId userId = UserId.getInstance(0L);

    /**
     * bankAccountId is a static final variable used to store the ID of the bank account. It is an instance of the BankAccountId class, which is a value object used to represent the unique identification of a bank account.
     * It is initialized with a default value of 0L, but its value can be changed later in the code.
     */
    private static final BankAccountId bankAccountId = BankAccountId.getInstance(0L);

    /**
     * The {@code paymentCardId} is an instance of the {@link PaymentCardId} class.
     * It is used to store and access the unique identifier of the payment card object.
     * This is a final variable, which means its value cannot be changed once it is assigned.
     * It is initialized with the value of 0L, but it should be set to the appropriate value when the payment card is created.
     */
    private static final PaymentCardId paymentCardId = PaymentCardId.getInstance(0L);

    /**
     * scanner is an instance of the Scanner class, used for reading input from the standard input stream.
     */
    private static Scanner scanner = new Scanner(System.in);
}
