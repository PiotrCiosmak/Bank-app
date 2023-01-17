package com.ciosmak.bankapp;

import com.ciosmak.bankapp.menu.Menu;
import com.ciosmak.bankapp.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * BankAppApplication is the main class of the bank application. It is responsible for starting the Spring Boot application,
 * logging in and providing access to the main menu.
 *
 * @author Author Piotr Ciosmak
 * @version 1.0
 * @see com.ciosmak.bankapp.menu.Menu
 * @see org.springframework.boot.CommandLineRunner
 * @see org.springframework.boot.SpringApplication
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 */
@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class BankAppApplication implements CommandLineRunner
{
    /**
     * Main method to start the application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args)
    {
        SpringApplication.run(BankAppApplication.class, args);
    }

    /**
     * Method that runs when the application starts. It displays the login menu and the main menu.
     *
     * @param args Command line arguments.
     */
    @Override
    public void run(String[] args)
    {
        Menu.loginMenu(userService, identityDocumentService, bankAccountService, paymentCardService, transferService);
        Menu.mainMenu(userService, personalDataService, addressService, identityDocumentService, bankAccountService, paymentCardService, transferService, historyService);
    }

    private final UserService userService;
    private final PersonalDataService personalDataService;
    private final AddressService addressService;
    private final IdentityDocumentService identityDocumentService;
    private final BankAccountService bankAccountService;
    private final PaymentCardService paymentCardService;
    private final TransferService transferService;
    private final HistoryService historyService;
}
