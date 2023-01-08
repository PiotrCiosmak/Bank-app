package com.ciosmak.bankapp.service;

import com.ciosmak.bankapp.entity.User;
import com.ciosmak.bankapp.repository.UserRepository;
import com.ciosmak.bankapp.user.id.UserId;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Scanner;

public class AbstractService
{
    protected String capitalizeFirstLetterOfEveryWord(String line)
    {
        StringBuilder result = new StringBuilder(line.length());
        String[] words = line.split("\\s+");
        for (int i = 0; i < words.length; ++i)
        {
            if (words[i].length() > 0)
            {
                result.append(Character.toUpperCase(words[i].charAt(0))).append(words[i].substring(1).toLowerCase()).append(" ");
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

    protected Optional<User> getUserById(UserId userId)
    {
        Optional<User> user = userRepository.findById(userId.getId());
        if (user.isEmpty())
        {
            System.err.println("BŁĄD KRYTYCZNY!!!");
            System.err.println("BRAK UŻYTKOWNIKA O TAKIM ID W BAZIE!!!");
            System.err.println("OPUSZCZANIE PROGRAMU");
            System.err.flush();
            System.exit(1);
        }
        return user;
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

    protected Scanner scanner = new Scanner(System.in);
    protected UserRepository userRepository;
}
