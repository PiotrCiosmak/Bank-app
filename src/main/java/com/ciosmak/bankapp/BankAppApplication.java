package com.ciosmak.bankapp;

import com.ciosmak.bankapp.menu.Menu;
import com.ciosmak.bankapp.service.BankAccountService;
import com.ciosmak.bankapp.service.UserService;
import com.ciosmak.bankapp.user.id.UserId;
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

        UserId userId = UserId.getInstance(0L);
        Menu.loginMenu(userService, userId, bankAccountService);
        Menu.mainMenu(userService, userId);
    }

    private final UserService userService;
    private final BankAccountService bankAccountService;
}
