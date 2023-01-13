package com.ciosmak.bankapp.service;

import com.ciosmak.bankapp.entity.IdentityDocument;
import com.ciosmak.bankapp.entity.User;
import com.ciosmak.bankapp.repository.UserRepository;
import com.ciosmak.bankapp.user.id.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Scanner;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class IdentityDocumentService extends AbstractService
{
    public void autoUpdateIdentityDocument(UserId userId)
    {
        User user = getUserById(userId, userRepository);
        if (user.getIdentityDocument().getExpiryDate().isBefore(LocalDate.now()))
        {
            updateIdentityDocument(userId, "\n---AUTOMATYCZNA AKTUALIZACJA DOWÓDU OSOBISTEGO---", "Twój dowód osobisty wygasł, wprowadź nowy, aby kontynuować.\n");
        }
    }

    public void updateIdentityDocument(UserId userId, String titleLabel, String descriptionLabel)
    {
        User user = getUserById(userId, userRepository);
        System.out.println(titleLabel);
        System.out.print(descriptionLabel);

        LocalDate releaseDate;
        while (true)
        {
            releaseDate = createDate();
            if (releaseDate.plusYears(10).isAfter(LocalDate.now()))
            {
                if (!releaseDate.isAfter(LocalDate.now()))
                {
                    user.getIdentityDocument().setReleaseDate(releaseDate);
                    break;
                }
                else
                {
                    System.err.println("Podana data wydania dowodu jest błędna.\nData wydania dowodu nie może być datą z przyszłości.");
                    System.err.flush();
                    System.out.println("Ponownie podaj datę wydania dowodu.");
                }
            }
            else
            {
                System.err.println("Podana data wydania dowodu jest błędna.\nDowód osobisty wydany w tym dniu jest już nie ważny.");
                System.err.flush();
                System.out.println("Ponownie podaj datę wydania dowodu.");
            }
        }
        LocalDate expiryDate = releaseDate.plusYears(10);
        user.getIdentityDocument().setExpiryDate(expiryDate);

        String seriesAndNumber;
        System.out.print("Podaj serię i numer dowodu: ");
        while (true)
        {
            scanner = new Scanner(System.in);
            seriesAndNumber = scanner.nextLine();
            seriesAndNumber = convertAllLettersToUppercase(seriesAndNumber);
            if (seriesAndNumberIsCorrect(seriesAndNumber))
            {
                user.getIdentityDocument().setSeriesAndNumber(seriesAndNumber);
                System.out.println("Dowód osobisty został zaktualizowany");
                break;
            }
            System.err.println("Podany numer i seria dowodu jest błędna.\nNumer i seria dowodu powinna się składać tylko z 9 znaków (3 litery, 6 cyfr).");
            System.err.flush();
            System.out.print("Ponownie podaj numer i serię dowodu: ");
        }
    }

    IdentityDocument createIdentityDocument()
    {
        IdentityDocument identityDocument = new IdentityDocument();
        System.out.println("---WPROWADŹ DOWÓD OSOBISTY---");

        LocalDate releaseDate;
        while (true)
        {
            releaseDate = createDate();
            if (releaseDate.plusYears(10).isAfter(LocalDate.now()))
            {
                if (!releaseDate.isAfter(LocalDate.now()))
                {
                    identityDocument.setReleaseDate(releaseDate);
                    break;
                }
                else
                {
                    System.err.println("Podana data wydania dowodu jest błędna.\nData wydania dowodu nie może być datą z przyszłości.");
                    System.err.flush();
                    System.out.println("Ponownie podaj datę wydania dowodu.");
                }
            }
            else
            {
                System.err.println("Podana data wydania dowodu jest błędna.\nDowód osobisty wydany w tym dniu jest już nie ważny.");
                System.err.flush();
                System.out.println("Ponownie podaj datę wydania dowodu.");
            }
        }
        LocalDate expiryDate = releaseDate.plusYears(10);
        identityDocument.setExpiryDate(expiryDate);

        String seriesAndNumber;
        System.out.print("Podaj serię i numer dowodu: ");
        while (true)
        {
            scanner = new Scanner(System.in);
            seriesAndNumber = scanner.nextLine();
            seriesAndNumber = convertAllLettersToUppercase(seriesAndNumber);
            if (seriesAndNumberIsCorrect(seriesAndNumber))
            {
                identityDocument.setSeriesAndNumber(seriesAndNumber);
                break;
            }
            else
            {
                System.err.println("Podany numer i seria dowodu jest błędna.\nNumer i seria dowodu powinna się składać tylko z 9 znaków (3 litery, 6 cyfr).");
                System.err.flush();
                System.out.print("Ponownie podaj numer i serię dowodu: ");
            }
        }
        return identityDocument;
    }

    private boolean seriesAndNumberIsCorrect(String seriesAndNumber)
    {
        if (seriesAndNumber.length() == 9)
        {
            for (int i = 0; i < 3; ++i)
            {
                if (!Character.isLetter(seriesAndNumber.charAt(i)))
                {
                    return false;
                }
            }
            for (int i = 3; i < 6; ++i)
            {
                if (!Character.isDigit(seriesAndNumber.charAt(i)))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private LocalDate createDate()
    {
        while (true)
        {
            try
            {
                System.out.println("---WPROWADŹ DATĘ WYDANIA---");
                int day;
                System.out.print("Podaj dzień: ");
                day = scanner.nextInt();

                int month;
                System.out.print("Podaj miesiąc: ");
                month = scanner.nextInt();

                int year;
                System.out.print("Podaj rok: ");
                year = scanner.nextInt();

                if (dateIsCorrect(day, month, year))
                {
                    return LocalDate.of(year, month, day);
                }
                else
                {
                    System.err.println("Podana data nie istnieje.\nSpróbuj ponownie.");
                    System.err.flush();
                }
            }
            catch (Exception e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Wystąpił błąd.\nNależy wprowadzać tylko cyfry.\nSpróbuj ponownie.");
                System.err.flush();
            }
        }
    }

    private boolean dateIsCorrect(Integer day, Integer month, Integer year)
    {
        if (year < 1)
        {
            return false;
        }

        if (month < 1 || month > 12)
        {
            return false;
        }

        if (day < 1)
        {
            return false;
        }

        switch (month)
        {
            case 1, 3, 5, 7, 8, 10, 12 ->
            {
                if (day > 31)
                {
                    return false;
                }
            }
            case 4, 6, 9, 11 ->
            {
                if (day > 30)
                {
                    return false;
                }
            }
            case 2 ->
            {
                if (year % 4 == 0)
                {
                    if (day > 29)
                    {
                        return false;
                    }
                }
                else
                {
                    if (day > 28)
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private final UserRepository userRepository;
}
