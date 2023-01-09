package com.ciosmak.bankapp.service;

import com.ciosmak.bankapp.entity.Address;
import com.ciosmak.bankapp.entity.IdentityDocument;
import com.ciosmak.bankapp.entity.PersonalData;
import com.ciosmak.bankapp.entity.User;
import com.ciosmak.bankapp.repository.PersonalDataRepository;
import com.ciosmak.bankapp.repository.UserRepository;
import com.ciosmak.bankapp.user.id.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService extends AbstractService
{
    public void register(UserId userId)
    {
        System.out.println("\n---REJESTRACJA---");

        String email;
        System.out.print("Podaj email: ");
        while (true)
        {
            email = scanner.nextLine();
            email = convertAllLettersToLowercase(email);
            if (emailIsCorrect(email))
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

        PersonalData personalData = createPersonalData();

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

        IdentityDocument identityDocument = createIdentityDocument();

        User user = User.builder().
                email(email).
                password(hash(password)).
                personalData(PersonalData.builder().firstName(personalData.getFirstName()).lastName(personalData.getLastName()).phoneNumber(personalData.getPhoneNumber()).familyName(personalData.getFamilyName()).personalIdentityNumber(personalData.getPersonalIdentityNumber()).birthPlace(personalData.getBirthPlace()).nationality(personalData.getNationality()).mothersName(personalData.getMothersName()).mothersMaidenName(personalData.getMothersMaidenName()).build()).
                addresses(addresses).
                identityDocument(IdentityDocument.builder().releaseDate(identityDocument.getReleaseDate()).expiryDate(identityDocument.getExpiryDate()).seriesAndNumber(identityDocument.getSeriesAndNumber()).build()).
                build();
        userRepository.save(user);
        userId.setId(user.getId());
    }

    public void signIn(UserId userId)
    {
        System.out.println("\n---LOGOWANIE---");

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
                if (checkIfHashedIsCorrect(password, user.get().getPassword()))
                {
                    System.out.println("Zalogowano");
                    userId.setId(user.get().getId());
                    break;
                }
                System.err.println("Błędne dane!!!\nSpróbuj ponownie");
            }

        }
    }

    public void changePassword(UserId userId)
    {
        User user = getUserById(userId, userRepository);
        String currentPassword, newPassword;

        System.out.println("\n---ZMIANA HASŁA---");
        while (true)
        {
            System.out.print("Podaj aktualne hasło: ");
            currentPassword = scanner.nextLine();
            if (checkIfHashedIsCorrect(currentPassword, user.getPassword()))
            {
                System.out.println("Podane hasło jest poprawne");
                System.out.print("Podaj nowe hasło: ");
                while (true)
                {
                    newPassword = scanner.nextLine();
                    if (passwordIsCorrect(newPassword))
                    {
                        break;
                    }
                    System.err.println("Podane hasło jest zbyt proste.\nHasło musi zawierać od 8 do 20 znaków, 1 wielką literę, 1 małą literę, 1 cyfrę, 1 znak specjalny.");
                    System.err.flush();
                    System.out.print("Ponownie podaj nowe hasło: ");
                }
                user.setPassword(hash(newPassword));
                break;
            }
            System.err.println("Niepoprawne hasło!!!\nSpróbuj ponownie");
        }

    }

    public void changeAddress(UserId userId)
    {
        User user = getUserById(userId, userRepository);

        Address address = createAddress("\n---ZMIANA ADRESU---", false);
        user.getAddresses().get(0).setStreet(address.getStreet());
        user.getAddresses().get(0).setHouseNumber(address.getHouseNumber());
        user.getAddresses().get(0).setApartmentNumber(address.getApartmentNumber());
        user.getAddresses().get(0).setPostCode(address.getPostCode());
        user.getAddresses().get(0).setTown(address.getTown());
        user.getAddresses().get(0).setCountry(address.getCountry());
        System.out.println("ADRES ZOSTAŁ ZMIENIONY POMYŚLNIE");
    }

    public void changeMailingAddress(UserId userId)
    {
        User user = getUserById(userId, userRepository);

        if (user.getAddresses().size() < 2)
        {
            char addMailingAddress;
            while (true)
            {
                System.out.println("Adres korespodencyjny jest taki sam jak adres główny.");
                System.out.println("Chcesz ustawić inny adres do korespondencji (T/N): ");
                addMailingAddress = scanner.nextLine().charAt(0);
                if (trueOrFalseAnswerIsCorrect(addMailingAddress))
                {
                    if (Character.toUpperCase(addMailingAddress) == 'T')
                    {
                        Address address = createAddress("\n---DODAWANIE ADRESU KORESPONDENCYJNEGO---", true);
                        user.getAddresses().add(address);
                    }
                    else
                    {
                        System.out.println("Żadne zmiany nie zostały wprowadzone");
                    }
                    return;
                }
                else
                {
                    System.err.println("Nie ma takiej opcji.\nNależy wprowadzić znak 'T' lub znak 'N'.\nSpróbuj ponownie.");
                    System.err.flush();
                }
            }
        }
        Address address = createAddress("---ZMIANA ADRESU KORESPONDENCYJNEGO---", true);
        user.getAddresses().get(1).setStreet(address.getStreet());
        user.getAddresses().get(1).setHouseNumber(address.getHouseNumber());
        user.getAddresses().get(1).setApartmentNumber(address.getApartmentNumber());
        user.getAddresses().get(1).setPostCode(address.getPostCode());
        user.getAddresses().get(1).setTown(address.getTown());
        user.getAddresses().get(1).setCountry(address.getCountry());
        System.out.println("ADRES KORESPONDENCYJNY ZOSTAŁ ZMIENIONY POMYŚLNIE");
    }

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

    public void updateIdentityDocument(UserId userId, String titleLabel, String descriptionLabel)
    {
        User user = getUserById(userId, userRepository);
        System.out.println(titleLabel);
        System.out.print(descriptionLabel);

        LocalDate releaseDate;
        while (true)
        {
            releaseDate = createDate("---WPROWADŹ DATĘ WYDANIA---");
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

    public void autoUpdateIdentityDocument(UserId userId)
    {
        User user = getUserById(userId, userRepository);
        if (user.getIdentityDocument().getExpiryDate().isBefore(LocalDate.now()))
        {
            updateIdentityDocument(userId, "\n---AUTOMATYCZNA AKTUALIZACJA DOWÓDU OSOBISTEGO---", "Twój dowód osobisty wygasł, wprowadź nowy, aby kontynuować.\n");
        }
    }

    private PersonalData createPersonalData()
    {
        PersonalData personalData = new PersonalData();
        System.out.println("---WPROWADŹ DANE OSOBOWE---");

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

        LocalDate releaseDate;
        while (true)
        {
            releaseDate = createDate("---WPROWADŹ DATĘ WYDANIA---");
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

    private final UserRepository userRepository;
    private final PersonalDataRepository personalDataRepository;
}
