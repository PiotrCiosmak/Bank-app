package com.ciosmak.bankapp;

import com.ciosmak.bankapp.menu.Menu;
import com.ciosmak.bankapp.service.BankAccountService;
import com.ciosmak.bankapp.service.PaymentCardService;
import com.ciosmak.bankapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class BankAppApplication implements CommandLineRunner
{
    public static void main(String[] args)
    {
        SpringApplication.run(BankAppApplication.class, args);
    }

    @Override
    public void run(String[] args)
    {

        Menu.loginMenu(userService, bankAccountService);
        Menu.mainMenu(userService, bankAccountService, paymentCardService);
    }

    private final UserService userService;
    private final BankAccountService bankAccountService;
    private final PaymentCardService paymentCardService;
}
