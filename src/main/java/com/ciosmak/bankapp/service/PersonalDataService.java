package com.ciosmak.bankapp.service;

import com.ciosmak.bankapp.entity.PersonalData;
import com.ciosmak.bankapp.entity.User;
import com.ciosmak.bankapp.exception.FatalError;
import com.ciosmak.bankapp.exception.IllegalOptionSelectedException;
import com.ciosmak.bankapp.repository.PersonalDataRepository;
import com.ciosmak.bankapp.repository.UserRepository;
import com.ciosmak.bankapp.user.id.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

/**
 * The PersonalDataService class contains methods for updating personal data of a user.
 * <p>This class provides methods for updating phone number, first name and last name of a user.
 * It also contains methods for checking the correctness of entered data.
 * <p>The updatePersonalData method allows the user to choose which data they want to update.
 * The phoneNumberIsCorrect and checkIfVarcharLengthIsNotCorrect methods check the correctness of entered data.
 *
 * @author Piotr Ciosmak
 * @version 1.0
 * @see AbstractService
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PersonalDataService extends AbstractService
{
    /**
     * This method allows the user to update their personal data.
     * The user can choose which data they want to update by selecting the appropriate option from the menu.
     *
     * @param userId the user's unique identification number
     */
    public void updatePersonalData(UserId userId)
    {
        User user = getUserById(userId, userRepository);
        int selectedOption;

        while (true)
        {
            try
            {
                System.out.println("\n---AKTUALIZOWANIE DANYCH OSOBOWYCH---");
                System.out.println("1. Zmień number telefonu");
                System.out.println("2. Zmień imię");
                System.out.println("3. Zmień nazwisko");
                System.out.println("4. Wstecz");
                System.out.print("Wybieram: ");
                selectedOption = scanner.nextInt();
                scanner = new Scanner(System.in);

                switch (selectedOption)
                {
                    case 1 ->
                    {
                        System.out.println("\n---ZMIANA NUMBERU TELEFONU---");
                        String phoneNumber;
                        System.out.print("Podaj numer telefonu: ");
                        while (true)
                        {
                            scanner = new Scanner(System.in);
                            phoneNumber = scanner.nextLine();
                            phoneNumber = preparePhoneNumber(phoneNumber);
                            if (phoneNumberIsCorrect(phoneNumber))
                            {
                                user.getPersonalData().setPhoneNumber(phoneNumber);
                                System.out.println("Numer telefonu został zaktualizowany.");
                                break;
                            }
                            System.err.println("Podany numer telefonu jest błędny.\nNumer telefonu powinien się składać tylko z 9 cyfr.");
                            System.err.flush();
                            System.out.print("Ponownie podaj numer telefonu: ");
                        }
                    }
                    case 2 ->
                    {
                        System.out.println("\n---ZMIANA IMIENIA---");
                        String firstName;
                        System.out.print("Podaj imie: ");
                        firstName = scanner.nextLine();
                        if (checkIfVarcharLengthIsNotCorrect(firstName))
                        {
                            FatalError.exit();
                        }
                        firstName = capitalizeFirstLetterOfEveryWord(firstName);
                        user.getPersonalData().setFirstName(firstName);
                        System.out.println("Imie został zaktualizowane.");
                    }
                    case 3 ->
                    {
                        System.out.println("\n---ZMIANA NAZWISKA---");
                        String lastName;
                        System.out.print("Podaj nazwisko: ");
                        lastName = scanner.nextLine();
                        if (checkIfVarcharLengthIsNotCorrect(lastName))
                        {
                            FatalError.exit();
                        }
                        lastName = capitalizeFirstLetterOfEveryWord(lastName);
                        user.getPersonalData().setLastName(lastName);
                        System.out.println("Nazwisko został zaktualizowane.");
                    }
                    case 4 ->
                    {
                        return;
                    }
                    default -> throw new IllegalOptionSelectedException("Nie ma takiej opcji.\nSpróbuj ponownie.\n", "");
                }
            }
            catch (InputMismatchException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do 4.\nSpróbuj ponownie.");
                System.err.flush();
            }
            catch (IllegalOptionSelectedException e)
            {
                e.show();
            }
            catch (Exception e)
            {
                FatalError.exit();
            }
        }
    }

    /**
     * Creates a new instance of PersonalData, prompts the user for input and sets the properties of the instance accordingly.
     * The user is prompted for input for the following fields: first name, last name, phone number, family name, personal identity number,
     * birthplace, nationality, mother's name, father's name.
     * Validates the input for each field to ensure that it meets the requirements for the field.
     *
     * @return A new instance of PersonalData with properties set according to user input.
     */
    PersonalData createPersonalData()
    {
        PersonalData personalData = new PersonalData();
        System.out.println("\n---WPROWADŹ DANE OSOBOWE---");

        String firstName;
        System.out.print("Podaj imie: ");
        firstName = scanner.nextLine();
        if (checkIfVarcharLengthIsNotCorrect(firstName))
        {
            FatalError.exit();
        }
        firstName = capitalizeFirstLetterOfEveryWord(firstName);
        personalData.setFirstName(firstName);

        String lastName;
        System.out.print("Podaj nazwisko: ");
        lastName = scanner.nextLine();
        if (checkIfVarcharLengthIsNotCorrect(lastName))
        {
            FatalError.exit();
        }
        lastName = capitalizeFirstLetterOfEveryWord(lastName);
        personalData.setLastName(lastName);

        String phoneNumber;
        System.out.print("Podaj numer telefonu: ");
        while (true)
        {
            phoneNumber = scanner.nextLine();
            phoneNumber = preparePhoneNumber(phoneNumber);
            if (phoneNumberIsCorrect(phoneNumber))
            {
                personalData.setPhoneNumber(phoneNumber);
                break;
            }
            else
            {
                System.err.println("Podany numer telefonu jest błędny.\nNumer telefonu powinien się składać tylko z 9 cyfr.");
                System.err.flush();
                System.out.print("Ponownie podaj numer telefonu: ");
            }
        }

        String familyName;
        System.out.print("Podaj nazwisko rodowe: ");
        familyName = scanner.nextLine();
        if (checkIfVarcharLengthIsNotCorrect(familyName))
        {
            FatalError.exit();
        }
        familyName = capitalizeFirstLetterOfEveryWord(familyName);
        personalData.setFamilyName(familyName);

        String personalIdentityNumber;
        System.out.print("Podaj number pesel: ");
        while (true)
        {
            personalIdentityNumber = scanner.nextLine();
            if (personalIdentityNumberIsCorrect(personalIdentityNumber) && personalIdentityNumberIsUnique(personalIdentityNumber))
            {
                personalData.setPersonalIdentityNumber(personalIdentityNumber);
                break;
            }
            else if (!personalIdentityNumberIsUnique(personalIdentityNumber))
            {
                System.err.println("Podany numer pesel jest zajęty.\nNumer pesel musi być unikatowy.");
                System.err.flush();
                System.out.print("Ponownie podaj numer pesel: ");
            }
            else
            {
                System.err.println("Podany numer pesel jest błędny.\nNumer pesel powinien się składać tylko z 11 cyfr.");
                System.err.flush();
                System.out.print("Ponownie podaj numer pesel: ");
            }
        }

        String birthPlace;
        System.out.print("Podaj miejsce urodzenia: ");
        birthPlace = scanner.nextLine();
        if (checkIfVarcharLengthIsNotCorrect(birthPlace))
        {
            FatalError.exit();
        }
        birthPlace = capitalizeFirstLetterOfEveryWord(birthPlace);
        personalData.setBirthPlace(birthPlace);

        String nationality;
        System.out.print("Podaj narodowość: ");
        nationality = scanner.nextLine();
        if (checkIfVarcharLengthIsNotCorrect(nationality))
        {
            FatalError.exit();
        }
        nationality = capitalizeFirstLetterOfEveryWord(nationality);
        personalData.setNationality(nationality);

        String mothersName;
        System.out.print("Podaj nazwisko matki: ");
        mothersName = scanner.nextLine();
        if (checkIfVarcharLengthIsNotCorrect(mothersName))
        {
            FatalError.exit();
        }
        mothersName = capitalizeFirstLetterOfEveryWord(mothersName);
        personalData.setMothersName(mothersName);

        String mothersMaidenName;
        System.out.print("Podaj nazwisko paniejskie matki: ");
        mothersMaidenName = scanner.nextLine();
        if (checkIfVarcharLengthIsNotCorrect(mothersMaidenName))
        {
            FatalError.exit();
        }
        mothersMaidenName = capitalizeFirstLetterOfEveryWord(mothersMaidenName);
        personalData.setMothersMaidenName(mothersMaidenName);
        return personalData;
    }

    /**
     * The method is responsible for checking whether the entered personal identity number is unique or not.
     *
     * @param personalIdentityNumber - entered personal identity number
     * @return true if the entered personal identity number is unique, false otherwise.
     */
    private boolean personalIdentityNumberIsUnique(String personalIdentityNumber)
    {
        Optional<PersonalData> personalData = personalDataRepository.findByPersonalIdentityNumber(personalIdentityNumber);
        return personalData.isEmpty();
    }

    /**
     * The personalIdentityNumberIsCorrect method is used to check if the given personal identity number (PESEL) is correct.
     * The method checks if the PESEL is 11 characters long and if each character is a digit.
     *
     * @param personalIdentityNumber - a string containing the PESEL number
     * @return true if the PESEL is 11 characters long and if each character is a digit.
     * @return false otherwise.
     */
    private boolean personalIdentityNumberIsCorrect(String personalIdentityNumber)
    {
        if (personalIdentityNumber.length() == 11)
        {
            for (int i = 0; i < personalIdentityNumber.length(); ++i)
            {
                if (!Character.isDigit(personalIdentityNumber.charAt(i)))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Prepare phone number by removing any non-numeric characters and
     * adding '+48' prefix if it is not present
     *
     * @param phoneNumber phone number as string
     * @return prepared phone number
     */
    private String preparePhoneNumber(String phoneNumber)
    {
        return ("+48" + phoneNumber).trim();
    }

    /**
     * This method checks if the phone number is correct.
     * The phone number should contain only digits and should have 12 characters.
     *
     * @param phoneNumber phone number that is being checked
     * @return true if phone number is correct, false otherwise
     */
    private boolean phoneNumberIsCorrect(String phoneNumber)
    {
        String phoneNumberWithoutCodeArea = phoneNumber.substring(3);
        return phoneNumberWithoutCodeArea.matches("^[0-9]*$") && phoneNumber.length() == 12;
    }

    /**
     * userRepository is an instance variable of type UserRepository, used to access and manipulate user data in the database.
     */
    private final UserRepository userRepository;

    /**
     * personalDataRepository is an instance variable of type PersonalDataRepository, used to access and manipulate user personal data in the database.
     */
    private final PersonalDataRepository personalDataRepository;
}
