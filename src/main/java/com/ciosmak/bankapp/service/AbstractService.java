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

public class AbstractService
{
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

    protected String convertAllLettersToLowercase(String line)
    {
        return line.toLowerCase().trim();
    }

    protected String convertAllLettersToUppercase(String line)
    {
        return line.toUpperCase().trim();
    }

    protected boolean trueOrFalseAnswerIsCorrect(Character answer)
    {
        return Character.toUpperCase(answer) == 'T' || Character.toUpperCase(answer) == 'N';
    }

    protected String hash(String password)
    {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12, new SecureRandom());
        return bCryptPasswordEncoder.encode(password);
    }

    protected boolean checkIfHashedIsCorrect(String plain, String hashed)
    {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        return bcrypt.matches(plain, hashed);
    }

    protected User getUserById(UserId userId, UserRepository userRepository)
    {
        Optional<User> user = userRepository.findById(userId.getId());
        if (user.isEmpty())
        {
            FatalError.exit();
        }
        return user.get();
    }

    protected BankAccount getBankAccountById(BankAccountId bankAccountId, BankAccountRepository bankAccountRepository)
    {
        Optional<BankAccount> bankAccount = bankAccountRepository.findById(bankAccountId.getId());
        if (bankAccount.isEmpty())
        {
            FatalError.exit();
        }
        return bankAccount.get();
    }

    public PaymentCard getPaymentCardById(PaymentCardId paymentCardId, PaymentCardRepository paymentCardRepository)
    {
        Optional<PaymentCard> paymentCard = paymentCardRepository.findById(paymentCardId.getId());
        if (paymentCard.isEmpty())
        {
            FatalError.exit();
        }
        return paymentCard.get();
    }

    protected boolean isNumberNegative(BigDecimal number)
    {
        return number.compareTo(BigDecimal.ZERO) < 0;
    }

    protected boolean checkIfCorrectProductIsSelected(int selectedProduct, int numberOfProducts)
    {
        return selectedProduct >= 0 && selectedProduct < numberOfProducts;
    }

    protected boolean checkIfVarcharLengthIsCorrect(String text)
    {
        return text.length() < 255;
    }

    protected boolean numberIsTooLong(BigDecimal number)
    {
        return number.toString().length() > 37;
    }

    protected Scanner scanner = new Scanner(System.in);
}
