package com.ciosmak.bankapp.service;

import com.ciosmak.bankapp.exception.IllegalDebtAmountException;
import com.ciosmak.bankapp.exception.IllegalLimitAmountException;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code AbstractPaymentCardService} is an abstract class that provides methods for managing payment cards.
 * <p>
 * It extends the {@link AbstractService} class.
 * <p>
 * It contains methods for preparing a debt balance, checking if a given BigDecimal number is correct,
 * <p>
 * preparing a PIN code, checking if a PIN code is correct, setting a limit per day and checking if a number is negative or too long.
 *
 * @author Piotr Ciosmak
 * @version 1.0
 * @see AbstractService
 */
public class AbstractPaymentCardService extends AbstractService
{
    /**
     * The method prepares a debt balance for a payment card and returns it as a BigDecimal number.
     * It has a parameter maxDebt, which is the maximum debt balance that can be set.
     * It takes input from the user and checks if the entered amount is correct,
     * if not it throws an exception and asks the user to enter the amount again.
     *
     * @param maxDebt the maximum debt balance that can be set
     * @return debt balance of the payment card as a BigDecimal number
     * @throws IllegalDebtAmountException when the entered debt amount is incorrect
     */
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

    /**
     * The method checks if a given BigDecimal number is correct,
     * it returns true if the number is greater than or equal to 0 and less than or equal to maxDebt.
     *
     * @param debt    the debt balance of the payment card
     * @param maxDebt the maximum debt balance that can be set
     * @return true if the number is correct, false otherwise
     */
    protected boolean checkIfBigDecimalIsCorrect(BigDecimal debt, BigDecimal maxDebt)
    {
        return debt.compareTo(BigDecimal.valueOf(0)) >= 0 && debt.compareTo(maxDebt) <= 0;
    }

    /**
     * The method prepares a PIN code for a payment card and returns it as a hashed string.
     * It takes input from the user and checks if the entered PIN is correct,
     * if not it asks the user to enter the PIN again.
     *
     * @return hashed PIN code of the payment card as a string
     */
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

    /**
     * The method checks if a given PIN code is correct,
     * it returns true if the PIN is a string of 4 digits, false otherwise.
     *
     * @param pin the PIN code of the payment card
     * @return true if the PIN code is correct, false otherwise
     */
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

    /**
     * The method sets the limit per day for a payment card.
     *
     * @param message The message to display to the user before entering the limit amount.
     * @return The set limit per day.
     * @throws IllegalLimitAmountException if the entered limit amount is invalid. The exception message provides more details on the error.
     */
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
