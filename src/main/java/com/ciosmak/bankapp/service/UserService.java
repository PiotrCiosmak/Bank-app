package com.ciosmak.bankapp.service;

import com.ciosmak.bankapp.entity.Address;
import com.ciosmak.bankapp.entity.IdentityDocument;
import com.ciosmak.bankapp.entity.PersonalData;
import com.ciosmak.bankapp.entity.User;
import com.ciosmak.bankapp.repository.UserRepository;
import com.ciosmak.bankapp.user.id.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
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

        PersonalData personalData = personalDataService.createPersonalData();

        ArrayList<Address> addresses = new ArrayList<>();
        addresses.add(addressService.createAddress("---WPROWADŹ ADRES---", false));

        addresses = addressService.createMailingAddress(addresses);

        IdentityDocument identityDocument = identityDocumentService.createIdentityDocument();

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
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!`~'\"^_&-+=()])(?=\\S+$).{8,20}$";
        return Pattern.compile(passwordRegex).matcher(password).matches();
    }

    private final UserRepository userRepository;
    private final AddressService addressService;
    private final IdentityDocumentService identityDocumentService;
    private final PersonalDataService personalDataService;
}
