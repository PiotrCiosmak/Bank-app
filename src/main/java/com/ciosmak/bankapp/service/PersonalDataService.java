package com.ciosmak.bankapp.service;

import com.ciosmak.bankapp.entity.PersonalData;
import com.ciosmak.bankapp.entity.User;
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

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PersonalDataService extends AbstractService
{
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
                            else
                            {
                                System.err.println("Podany numer telefonu jest błędny.\nNumer telefonu powinien się składać tylko z 9 cyfr.");
                                System.err.flush();
                                System.out.print("Ponownie podaj numer telefonu: ");
                            }
                        }
                    }
                    case 2 ->
                    {
                        System.out.println("\n---ZMIANA IMIENIA---");
                        String firstName;
                        System.out.print("Podaj imie: ");
                        firstName = scanner.nextLine();
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
                        lastName = capitalizeFirstLetterOfEveryWord(lastName);
                        user.getPersonalData().setLastName(lastName);
                        System.out.println("Nazwisko został zaktualizowane.");
                    }
                    case 4 ->
                    {
                        return;
                    }
                    default ->
                    {
                        System.err.println("Nie ma takiej opcji.\nSpróbuj ponownie.");
                        System.err.flush();
                    }
                }
            }
            catch (InputMismatchException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do 4.\nSpróbuj ponownie.");
                System.err.flush();
            }
            catch (Exception e)
            {
                System.err.println("BŁĄD KRYTYCZNY!!!");
                System.err.println("OPUSZCZANIE PROGRAMU");
                System.err.flush();
                System.exit(1);
            }
        }
    }

    PersonalData createPersonalData()
    {
        PersonalData personalData = new PersonalData();
        System.out.println("\n---WPROWADŹ DANE OSOBOWE---");

        String firstName;
        System.out.print("Podaj imie: ");
        firstName = scanner.nextLine();
        firstName = capitalizeFirstLetterOfEveryWord(firstName);
        personalData.setFirstName(firstName);

        String lastName;
        System.out.print("Podaj nazwisko: ");
        lastName = scanner.nextLine();
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
        birthPlace = capitalizeFirstLetterOfEveryWord(birthPlace);
        personalData.setBirthPlace(birthPlace);

        String nationality;
        System.out.print("Podaj narodowość: ");
        nationality = scanner.nextLine();
        nationality = capitalizeFirstLetterOfEveryWord(nationality);
        personalData.setNationality(nationality);

        String mothersName;
        System.out.print("Podaj nazwisko matki: ");
        mothersName = scanner.nextLine();
        mothersName = capitalizeFirstLetterOfEveryWord(mothersName);
        personalData.setMothersName(mothersName);

        String mothersMaidenName;
        System.out.print("Podaj nazwisko paniejskie matki: ");
        mothersMaidenName = scanner.nextLine();
        mothersMaidenName = capitalizeFirstLetterOfEveryWord(mothersMaidenName);
        personalData.setMothersMaidenName(mothersMaidenName);
        return personalData;
    }

    private boolean personalIdentityNumberIsUnique(String personalIdentityNumber)
    {
        Optional<PersonalData> personalData = personalDataRepository.findByPersonalIdentityNumber(personalIdentityNumber);
        return personalData.isEmpty();
    }

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

    private String preparePhoneNumber(String phoneNumber)
    {
        return ("+48" + phoneNumber).trim();
    }

    private boolean phoneNumberIsCorrect(String phoneNumber)
    {
        if (phoneNumber.length() == 12)
        {
            for (int i = 3; i < phoneNumber.length(); ++i)
            {
                if (!Character.isDigit(phoneNumber.charAt(i)))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private final UserRepository userRepository;
    private final PersonalDataRepository personalDataRepository;
}
