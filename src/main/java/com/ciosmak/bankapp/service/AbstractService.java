package com.ciosmak.bankapp.service;

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

    protected Scanner scanner = new Scanner(System.in);
}
