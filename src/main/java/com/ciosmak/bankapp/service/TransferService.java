package com.ciosmak.bankapp.service;

import com.ciosmak.bankapp.entity.Transfer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TransferService extends AbstractService
{
    public void create()
    {
        Transfer transfer = new Transfer();
        System.out.println("\n---WYKONAJ PRZELEW---");

        String title;
        System.out.println("Podaj tytuł przelewu: ");
        title = scanner.nextLine();


        BigDecimal amountOfMoney;
        while (true)
        {
            try
            {
                System.out.println("Podaj kwotę: ");
                amountOfMoney = scanner.nextBigDecimal();
                Pattern pattern = Pattern.compile("\\d+\\.(\\d{3,})");
                Matcher matcher = pattern.matcher(amountOfMoney.toString());
                if (isNumberNegative(amountOfMoney))
                {
                    throw new IllegalAccessException();
                }
                if (!matcher.find())
                {
                    break;
                }
                throw new InputMismatchException();
            }
            catch (InputMismatchException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Podana kwota jest błędna.\nKwota powinna się być liczbą z maksymalnie dwiema cyframi po przecinku.\nSpróbuj ponownie.");
                System.err.flush();
            }
            catch (IllegalAccessException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Podana kwota jest błędna.\nKwota nie może byc ujemna.\nSpróbuj ponownie");
                System.err.flush();
            }
            catch (Exception e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Podana kwota jest błędna.\nKwota jest zbyt dużą liczbą.\nSpróbuj ponownie.");
                System.err.flush();
            }
        }
    }
}
