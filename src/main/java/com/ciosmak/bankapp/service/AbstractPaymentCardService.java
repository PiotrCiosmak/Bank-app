package com.ciosmak.bankapp.service;

import com.ciosmak.bankapp.exception.IllegalDebtAmountException;
import com.ciosmak.bankapp.exception.IllegalLimitAmountException;

import java.math.BigDecimal;
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
                    throw new IllegalDebtAmountException("Podana kwota limitu debetowego jest błędna.\nKwota limitu debetowego powinna być liczbą z zakresu od 0 do " + maxDebt + ".\nSpróbuj ponownie.\n", "");
                }
            }
            catch (IllegalDebtAmountException e)
            {
                scanner = new Scanner(System.in);
                e.show();
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
                    throw new IllegalLimitAmountException("Podana kwota limitu jest błędna.\nKwota limitu nie może być liczbą ujemną.\nSpróbuj ponownie.\n", "");
                }
                if (numberIsTooLong(limitPerDay))
                {
                    throw new IllegalLimitAmountException("Podana kwota limitu jest błędna.\nKwota limitu nie może być aż tak duża.\nSpróbuj ponownie.\n", "");
                }
                if (!matcher.find())
                {
                    return limitPerDay;
                }
                throw new IllegalLimitAmountException("Podana kwota limitu jest błędna.\nKwota limitu powinna być liczbą z maksymalnie dwoma cyframi po przecinku.\nSpróbuj ponownie.\n", "");
            }
            catch (IllegalLimitAmountException e)
            {
                scanner = new Scanner(System.in);
                e.show();
            }
        }
    }
}
