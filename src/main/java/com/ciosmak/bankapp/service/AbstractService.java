package com.ciosmak.bankapp.service;

import com.ciosmak.bankapp.bank.account.id.BankAccountId;
import com.ciosmak.bankapp.entity.BankAccount;
import com.ciosmak.bankapp.entity.PaymentCard;
import com.ciosmak.bankapp.entity.User;
import com.ciosmak.bankapp.exception.FatalError;
import com.ciosmak.bankapp.payment.card.id.PaymentCardId;
import com.ciosmak.bankapp.repository.BankAccountRepository;
import com.ciosmak.bankapp.repository.PaymentCardRepository;
import com.ciosmak.bankapp.repository.UserRepository;
import com.ciosmak.bankapp.user.id.UserId;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Scanner;

/**
 * The AbstractService class provides various utility methods for the bank application.
 *
 * @author Piotr Ciosmak
 * @version 1.0
 */
public class AbstractService
{
    /**
     * Capitalizes the first letter of every word in the given line.
     *
     * @param line a string to be capitalized
     * @return the capitalized string
     */
    protected String capitalizeFirstLetterOfEveryWord(String line)
    {
        StringBuilder result = new StringBuilder(line.length());
        String[] words = line.split("\\s+");
        for (String word : words)
        {
            if (word.length() > 0)
            {
                result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase()).append(" ");
            }
        }
        return result.toString().trim();
    }

    /**
     * Converts all letters in the given line to lowercase.
     *
     * @param line a string to be converted to lowercase
     * @return the lowercased string
     */
    protected String convertAllLettersToLowercase(String line)
    {
        return line.toLowerCase().trim();
    }

    /**
     * Converts all letters in the given line to uppercase.
     *
     * @param line a string to be converted to uppercase
     * @return the uppercased string
     */
    protected String convertAllLettersToUppercase(String line)
    {
        return line.toUpperCase().trim();
    }

    /**
     * Checks if the given answer is a 'T' or 'N'
     *
     * @param answer character of the answer
     * @return true if answer is 'T' or 'N', false otherwise
     */
    protected boolean trueOrFalseAnswerIsCorrect(Character answer)
    {
        return Character.toUpperCase(answer) == 'T' || Character.toUpperCase(answer) == 'N';
    }

    /**
     * Hashes the given plaintext password.
     *
     * @param password plaintext password to be hashed
     * @return the hashed password
     */
    protected String hash(String password)
    {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12, new SecureRandom());
        return bCryptPasswordEncoder.encode(password);
    }

    /**
     * Checks if the plaintext password matches the given hashed password.
     *
     * @param plain  plaintext password
     * @param hashed hashed password
     * @return true if the plaintext password matches the hashed password, false otherwise
     */
    protected boolean checkIfHashedIsCorrect(String plain, String hashed)
    {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        return bcrypt.matches(plain, hashed);
    }

    /**
     * Retrieves the user with the given userId from the user repository.
     *
     * @param userId         the id of the user to retrieve
     * @param userRepository the repository to retrieve the user from
     * @return the user with the given id
     */
    protected User getUserById(UserId userId, UserRepository userRepository)
    {
        Optional<User> user = userRepository.findById(userId.getId());
        if (user.isEmpty())
        {
            FatalError.exit();
        }
        return user.get();
    }

    /**
     * Retrieves the bank account with the given bankAccountId from the bank account repository.
     *
     * @param bankAccountId         the id of the bank account to retrieve
     * @param bankAccountRepository the repository to retrieve the bank account from
     * @return the bank account with the given id
     */
    protected BankAccount getBankAccountById(BankAccountId bankAccountId, BankAccountRepository bankAccountRepository)
    {
        Optional<BankAccount> bankAccount = bankAccountRepository.findById(bankAccountId.getId());
        if (bankAccount.isEmpty())
        {
            FatalError.exit();
        }
        return bankAccount.get();
    }

    /**
     * Retrieves the payment card with the given paymentCardId from the payment card repository.
     *
     * @param paymentCardId         the id of the payment card to retrieve
     * @param paymentCardRepository the repository to retrieve the payment card from
     * @return the payment card with the given id
     */
    public PaymentCard getPaymentCardById(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        Optional<PaymentCard> paymentCard = paymentCardRepository.findById(paymentCardId.getId());
        if (paymentCard.isEmpty())
        {
            FatalError.exit();
        }
        return paymentCard.get();
    }

    /**
     * Checks if the given number is negative
     *
     * @param number a number to be checked
     * @return true if the number is negative, false otherwise
     */
    protected boolean isNumberNegative(BigDecimal number)
    {
        return number.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * Checks if the selected product is within the valid range of products
     *
     * @param selectedProduct  the selected product number
     * @param numberOfProducts the total number of products
     * @return true if the selected product is within the valid range, false otherwise
     */
    protected boolean checkIfCorrectProductIsSelected(int selectedProduct, int numberOfProducts)
    {
        return selectedProduct >= 0 && selectedProduct < numberOfProducts;
    }

    /**
     * Checks if the length of the given text is greater than or equal to 255
     *
     * @param text the text to be checked
     * @return true if the text length is greater than or equal to 255, false otherwise
     */
    protected boolean checkIfVarcharLengthIsNotCorrect(String text)
    {
        return text.length() >= 255;
    }

    /**
     * Checks if the given number is too long to be stored
     *
     * @param number the number to be checked
     * @return true if the number is too long to be stored, false otherwise
     */
    protected boolean numberIsTooLong(BigDecimal number)
    {
        return number.toString().length() > 37;
    }

    /**
     * A Scanner object for reading input from the standard input.
     */
    protected Scanner scanner = new Scanner(System.in);
}
