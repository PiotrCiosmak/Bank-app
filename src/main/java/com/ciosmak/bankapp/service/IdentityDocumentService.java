package com.ciosmak.bankapp.service;

import com.ciosmak.bankapp.entity.IdentityDocument;
import com.ciosmak.bankapp.entity.User;
import com.ciosmak.bankapp.exception.FatalError;
import com.ciosmak.bankapp.exception.InvalidDateException;
import com.ciosmak.bankapp.repository.UserRepository;
import com.ciosmak.bankapp.user.id.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This class represents the service of Identity Document which is used to update or auto update the identity document of a user.
 * It contains methods for updating and auto updating the identity document of a user.
 *
 * @author Piotr Ciosmak
 * @version 1.0
 * @see AbstractService
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class IdentityDocumentService extends AbstractService
{
    /**
     * This method auto updates the identity document of a user if the expiry date of the document is before the current date.
     *
     * @param userId the id of the user whose identity document is to be auto updated.
     */
    public void autoUpdateIdentityDocument(UserId userId)
    {
        User user = getUserById(userId, userRepository);
        if (user.getIdentityDocument().getExpiryDate().isBefore(LocalDate.now()))
        {
            updateIdentityDocument(userId, "\n---AUTOMATYCZNA AKTUALIZACJA DOWÓDU OSOBISTEGO---", "Twój dowód osobisty wygasł, wprowadź nowy, aby kontynuować.\n");
        }
    }

    /**
     * This method updates the identity document of a user, it takes the release date, expiry date and series and number of the document as input.
     *
     * @param userId           the id of the user whose identity document is to be updated.
     * @param titleLabel       label to be displayed as title of the update process
     * @param descriptionLabel label to be displayed as description of the update process
     */
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

    /**
     * The method creates a new IdentityDocument object and prompts the user to input the release date, expiry date, and series and number of the document.
     * The release date and expiry date are calculated by adding 10 years to the release date, and the series and number must be 9 characters long, with the first 3 characters being letters and the next 6 being digits.
     *
     * @return A new IdentityDocument object with the user-specified release date, expiry date, and series and number.
     */
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

    /**
     * The method checks if the series and number of the identity document is in the correct format (3 letters and 6 digits)
     *
     * @param seriesAndNumber The series and number of the identity document
     * @return A boolean value indicating whether the series and number is in the correct format or not
     */
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

    /**
     * This method creates a date input by the user.
     *
     * @return LocalDate - a date created by user.
     */
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
                    throw new InvalidDateException("Podana data nie istnieje.\nSpróbuj ponownie.\n", "");
                }
            }
            catch (InvalidDateException e)
            {
                e.show();
            }
            catch (InputMismatchException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Wystąpił błąd.\nNależy wprowadzać tylko cyfry.\nSpróbuj ponownie.");
                System.err.flush();
            }
            catch (Exception e)
            {
                FatalError.exit();
            }
        }
    }

    /**
     * The method checks if the date entered is a valid date.
     *
     * @param day   The day of the date to be checked.
     * @param month The month of the date to be checked.
     * @param year  The year of the date to be checked.
     * @return true if date is correct, false otherwise.
     */
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

    /**
     * userRepository is an instance variable of type UserRepository, used to access and manipulate user data in the database.
     */
    private final UserRepository userRepository;
}
