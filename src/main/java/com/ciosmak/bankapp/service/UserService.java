package com.ciosmak.bankapp.service;

import com.ciosmak.bankapp.entity.Address;
import com.ciosmak.bankapp.entity.IdentityDocument;
import com.ciosmak.bankapp.entity.PersonalData;
import com.ciosmak.bankapp.entity.User;
import com.ciosmak.bankapp.repository.PersonalDataRepository;
import com.ciosmak.bankapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor

@Transactional
@Service
public class UserService extends AbstractService
{
    public void register()
    {
        System.out.println("---REJESTRACJA---");

        String email;
        System.out.print("Podaj email: ");
        while (true)
        {
            email = scanner.nextLine();
            email = convertAllLettersToLowercase(email);
            if (emailIsCorrect(email)) //weenatrz czy email jest unique
            {
                if (emailIsUnique(email))
                {
                    break;
                }
                System.err.println("Podany email został wykorzystany przez innego użytkownika.");
            }
            else
            {
                System.err.println("Podany email jest nie poprawny.");
            }
            System.err.flush();
            System.out.print("Ponownie podaj email: ");
        }

        String password;
        System.out.print("Podaj hasło: ");
        while (true)
        {
            password = scanner.nextLine();
            if (passwordIsCorrect(password))
            {
                break;
            }
            System.err.println("Podane hasło jest zbyt proste.\nHasło musi zawierać od 8 do 20 znaków, 1 wielką literę, 1 małą literę, 1 cyfrę, 1 znak specjalny.");
            System.err.flush();
            System.out.print("Ponownie podaj hasło: ");
        }

        PersonalData personalDataTmp = new PersonalData();
        personalDataTmp = createPersonalData();

        ArrayList<Address> addresses = new ArrayList<>();
        addresses.add(createAddress("---WPROWADŹ ADRES---", false));

        char mailingAddressIsDifferent;
        while (true)
        {
            try
            {
                System.out.print("Chcesz podać inny adres do kontaktu (T/N): ");
                mailingAddressIsDifferent = scanner.nextLine().charAt(0);
                if (trueOrFalseAnswerIsCorrect(mailingAddressIsDifferent))
                {
                    if (Character.toUpperCase(mailingAddressIsDifferent) == 'T')
                    {
                        addresses.add(createAddress("---WPROWADŹ ADRES KORESPONDENCYJNY---", true));
                    }
                    break;
                }
                else
                {
                    System.err.println("Nie ma takiej opcji.\nNależy wprowadzić znak 'T' lub znak 'N'.\nSpróbuj ponownie.");
                    System.err.flush();
                }

            }
            catch (Exception e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić znak 'T' lub znak 'N'.\nSpróbuj ponownie.");
                System.err.flush();
            }
        }

        IdentityDocument identityDocumentTmp = createIdentityDocument();

        User user = User.builder().
                email(email).
                password(hash(password)).
                personalData(PersonalData.builder().firstName(personalDataTmp.getFirstName()).lastName(personalDataTmp.getLastName()).phoneNumber(personalDataTmp.getPhoneNumber()).familyName(personalDataTmp.getFamilyName()).personalIdentityNumber(personalDataTmp.getPersonalIdentityNumber()).birthPlace(personalDataTmp.getBirthPlace()).nationality(personalDataTmp.getNationality()).mothersName(personalDataTmp.getMothersName()).mothersMaidenName(personalDataTmp.getMothersMaidenName()).build()).
                addresses(addresses).
                identityDocument(IdentityDocument.builder().releaseDate(identityDocumentTmp.getReleaseDate()).expiryDate(identityDocumentTmp.getExpiryDate()).seriesAndNumber(identityDocumentTmp.getSeriesAndNumber()).build())
                .build();
        userRepository.save(user);
        //TODO USTAWIENIE USER_ID (SINGLETON)
    }

    public void signIn()
    {
        System.out.println("---LOGOWANIE---");

        String email, password;
        Optional<User> user;

        while (true)
        {
            System.out.print("Podaj email: ");
            email = scanner.nextLine();
            email = convertAllLettersToLowercase(email);

            System.out.print("Podaj hasło: ");
            password = scanner.nextLine();
            user = userRepository.findByEmail(email);
            if (user.isEmpty())
            {
                System.err.println("Błędne dane!!!\nSpróbuj ponownie");
            }
            else
            {
                if (checkIfPasswordIsCorrect(password, user.get().getPassword()))
                {
                    System.out.println("Zalogowano");
                    //TODO USTAWIENIE USER_ID (SINGLETON)
                    break;
                }
                System.err.println("Błędne dane!!!\nSpróbuj ponownie");
            }

        }
    }

    private PersonalData createPersonalData()
    {
        PersonalData personalData = new PersonalData();
        System.out.println("---WPROWADŹ DANE OSBOWE---");
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

    private Address createAddress(String label, boolean isMailing)
    {
        Address address = new Address();
        System.out.println(label);

        String street;
        System.out.print("Podaj ulicę: ");
        street = scanner.nextLine();
        street = capitalizeFirstLetterOfEveryWord(street);
        address.setStreet(street);

        String houseNumber;
        System.out.print("Podaj numer domu: ");
        houseNumber = scanner.nextLine();
        houseNumber = convertAllLettersToUppercase(houseNumber);
        address.setHouseNumber(houseNumber);

        String apartmentNumber;
        System.out.print("Podaj numer mieszkania: ");
        apartmentNumber = scanner.nextLine();
        apartmentNumber = convertAllLettersToUppercase(apartmentNumber);
        address.setApartmentNumber(apartmentNumber);

        String postCode;
        System.out.print("Podaj kod pocztowy: ");
        while (true)
        {
            postCode = scanner.nextLine();
            postCode = preparePostCode(postCode);
            if (postCodeIsCorrect(postCode))
            {
                break;
            }
            else
            {
                System.err.println("Podany kod pocztowy jest błędny.\nKod pocztowy powinien się składać tylko z 5 cyfr.");
                System.err.flush();
                System.out.print("Ponownie podaj kod pocztowy: ");
            }
        }
        address.setPostCode(postCode);

        String town;
        System.out.print("Podaj miejscowość: ");
        town = scanner.nextLine();
        town = capitalizeFirstLetterOfEveryWord(town);
        address.setTown(town);

        String country;
        System.out.print("Podaj kraj: ");
        country = scanner.nextLine();
        country = capitalizeFirstLetterOfEveryWord(country);
        address.setCountry(country);

        address.setMailing(isMailing);
        return address;
    }

    private IdentityDocument createIdentityDocument()
    {
        IdentityDocument identityDocument = new IdentityDocument();
        System.out.println("---WPROWADŹ DOWÓD OSOBISTY---");

        LocalDate releaseDate = createDate("---WPROWADŹ DATĘ WYDANIA---");
        identityDocument.setReleaseDate(releaseDate);

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

    private LocalDate createDate(String label)
    {
        while (true)
        {
            try
            {
                System.out.println(label);
                Integer day;
                System.out.print("Podaj dzień: ");
                day = scanner.nextInt();

                Integer month;
                System.out.print("Podaj miesiąc: ");
                month = scanner.nextInt();

                Integer year;
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

    private boolean personalIdentityNumberIsUnique(String personalIdentityNumber)
    {
        Optional<PersonalData> personalData = personalDataRepository.findByPersonalIdentityNumber(personalIdentityNumber);
        return personalData.isEmpty();
    }

    private boolean emailIsCorrect(String email)
    {
        String emailRegex = "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@" + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    private boolean emailIsUnique(String email)
    {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isEmpty();
    }

    private boolean passwordIsCorrect(String password)
    {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^_&-+=()])(?=\\S+$).{8,20}$";
        return Pattern.compile(passwordRegex).matcher(password).matches();
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

    private String preparePostCode(String postCode)
    {
        if (postCode.charAt(2) == '-')
        {
            return postCode;
        }
        return postCode.substring(0, 2) + "-" + postCode.substring(2);
    }

    private boolean postCodeIsCorrect(String postCode)
    {
        if (postCode.length() == 6)
        {
            for (int i = 0; i < postCode.length(); ++i)
            {
                if (i == 2)
                {
                    continue;
                }
                if (!Character.isDigit(postCode.charAt(i)))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean trueOrFalseAnswerIsCorrect(Character answer)
    {
        return Character.toUpperCase(answer) == 'T' || Character.toUpperCase(answer) == 'N';
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

    protected boolean seriesAndNumberIsCorrect(String seriesAndNumber)
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

    private String hash(String password)
    {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12, new SecureRandom());
        return bCryptPasswordEncoder.encode(password);
    }

    private boolean checkIfPasswordIsCorrect(String password, String hashedPassword)
    {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        return bcrypt.matches(password, hashedPassword);
    }

    private final UserRepository userRepository;
    private final PersonalDataRepository personalDataRepository;

}
