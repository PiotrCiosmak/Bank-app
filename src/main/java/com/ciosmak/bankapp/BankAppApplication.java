package com.ciosmak.bankapp;

import com.ciosmak.bankapp.menu.Menu;
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
        String loginOption = Menu.mainLoginMenu();
        UserId userId = UserId.getInstance(0L);
        switch (loginOption)
        {
            case "sign_in" -> userService.signIn(userId);
            case "register" -> userService.register(userId);
        }
    }

    private final UserService userService;

}
