package com.ciosmak.bankapp.service;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AbstractPaymentCardService extends AbstractService
{
    protected BigDecimal prepareDebtBalance(BigDecimal maxDebt)
    {
        BigDecimal debt;
        while (true)
        {
            try
            {
                System.out.println("---USTAWIENIE BALANSU DEBETOWEGO---");
                System.out.println("Maksymalny dostępna kwota: " + maxDebt.toString() + " zł");
                System.out.print("Kwota limitu debetowego: ");
                debt = scanner.nextBigDecimal();
                Pattern pattern = Pattern.compile("\\d+\\.(\\d{3,})");
                Matcher matcher = pattern.matcher(debt.toString());
                if (checkIfBigDecimalIsCorrect(debt, maxDebt))
                {
                    if (!matcher.find())
                    {
                        return debt;
                    }
                    throw new InputMismatchException();
                }
                System.err.println("Podana kwota limitu debetowego jest błędna.\nKwota limitu debetowego powinna się być liczbą z zakresu od 0 do " + maxDebt.toString() + ".\nSpróbuj ponownie.");
                System.err.flush();
            }
            catch (InputMismatchException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Podana kwota limitu debetowego jest błędna.\nKwota limitu debetowego powinna się być liczbą z maksymalnie dwiema cyframi po przecinku.\nSpróbuj ponownie.");
                System.err.flush();
            }
        }
    }

    protected boolean checkIfBigDecimalIsCorrect(BigDecimal debt, BigDecimal maxDebt)
    {
        return debt.compareTo(BigDecimal.valueOf(0)) >= 0 && debt.compareTo(maxDebt) <= 0;
    }

    protected String preparePin()
    {
        String pin;
        System.out.print("Podaj kod pin: ");
        while (true)
        {
            pin = scanner.nextLine();
            if (pinIsCorrect(pin))
            {
                break;
            }
            System.err.println("Podany kod pin jest błędny.\nKod pin powinien się składać tylko z 4 cyfr.");
            System.err.flush();
            System.out.print("Ponownie podaj kodu pin: ");
        }
        return hash(pin);
    }

    protected boolean pinIsCorrect(String pin)
    {
        if (pin.length() == 4)
        {
            for (int i = 0; i < pin.length(); ++i)
            {
                if (!Character.isDigit(pin.charAt(i)))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    protected BigDecimal setLimitPerDay(String message)
    {
        BigDecimal limitPerDay;
        while (true)
        {
            try
            {
                System.out.print(message);
                limitPerDay = scanner.nextBigDecimal();
                Pattern pattern = Pattern.compile("\\d+\\.(\\d{3,})");
                Matcher matcher = pattern.matcher(limitPerDay.toString());
                if (isNumberNegative(limitPerDay))
                {
                    throw new IllegalAccessException();
                }
                if (numberIsTooLong(limitPerDay))
                {
                    throw new Exception();
                }
                if (!matcher.find())
                {
                    return limitPerDay;
                }
                throw new InputMismatchException();
            }
            catch (InputMismatchException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Podana kwota limitu jest błędna.\nKwota limitu powinna się być liczbą z maksymalnie dwiema cyframi po przecinku.\nSpróbuj ponownie.");
                System.err.flush();
            }
            catch (IllegalAccessException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Podana kwota limitu jest błędna.\nKwota limitu nie może byc liczbą ujemną.\nSpróbuj ponownie");
                System.err.flush();
            }
            catch (Exception e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Podana kwota limitu jest błędna.\nKwota limitu jest zbyt dużą liczbą.\nSpróbuj ponownie.");
                System.err.flush();
            }
        }
    }

    private boolean numberIsTooLong(BigDecimal number)
    {
        return number.toString().length() > 37;
    }
}
