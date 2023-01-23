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

/**
 * The UserService class is responsible for registering and signing in users. It uses the UserRepository to save and retrieve user data.
 * It also uses the PersonalDataService, AddressService, and IdentityDocumentService to create and store personal data, addresses, and identity documents for the user.
 * The class also contains methods for validating email and password input, and for hashing the password for security.
 *
 * @author Piotr Ciosmak
 * @version 1.0
 * @see AbstractService
 */
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService extends AbstractService
{
    /**
     * The register method is responsible for registering a new user in the system. It prompts the user for their email and password and validates the input using the emailIsCorrect and passwordIsCorrect methods.
     * The method also creates and stores personal data, addresses, and an identity document for the user using the PersonalDataService, AddressService, and IdentityDocumentService classes.
     * The user's password is also hashed for security before being stored in the system. The user's id is also set in the UserId object passed as a parameter.
     *
     * @param userId a UserId object used to set the id of the newly created user
     */
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

    /**
     * The signIn method is responsible for signing in an existing user. It prompts the user for their email and password, and checks if the email and hashed password match any existing users in the system.
     * If the email and password match, the user is signed in and the id of the user is set in the UserId object passed as a parameter.
     * If the email or password do not match, the user is prompted to try again.
     *
     * @param userId a UserId object used to set the id of the signed in user
     */
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

    /**
     * The changePassword method allows a user to change their password. It prompts the user for their current password and then for a new password.
     * The method first checks if the current password matches the hashed password of the user in the system.
     * If the current password is correct, the new password is then validated using the passwordIsCorrect method.
     * If the new password is valid, the new password is hashed and saved to the system and the user's password is updated.
     *
     * @param userId a UserId object used to identify the user whose password is being changed
     */
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

    /**
     * The emailIsCorrect method checks whether the given email address is in a valid format.
     * It uses a regular expression to match the email against a pattern and returns a boolean indicating whether the email is valid or not.
     *
     * @param email a string representing an email address
     * @return true if the email is in a valid format, false otherwise
     */
    private boolean emailIsCorrect(String email)
    {
        String emailRegex = "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@" + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    /**
     * The emailIsUnique method checks whether the given email address is unique in the system or if it has already been used by another user.
     * It queries the user repository to check if the email address is already associated with an existing user.
     *
     * @param email a string representing an email address
     * @return true if the email address is unique and not associated with an existing user, false otherwise.
     */
    private boolean emailIsUnique(String email)
    {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isEmpty();
    }

    /**
     * The passwordIsCorrect method checks whether the given password is in a valid format.
     * It uses a regular expression to match the password against a pattern that specifies the password must have between 8 and 20 characters, at least one digit, one uppercase letter, one lowercase letter, and one special character.
     * The method returns a boolean indicating whether the password is valid or not.
     *
     * @param password a string representing a password
     * @return true if the password is in a valid format, false otherwise
     */
    private boolean passwordIsCorrect(String password)
    {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!`~'\"^_&-+=()])(?=\\S+$).{8,20}$";
        return Pattern.compile(passwordRegex).matcher(password).matches();
    }

    /**
     * userRepository is an instance variable of type UserRepository, used to access and manipulate user data in the database.
     */
    private final UserRepository userRepository;

    /**
     * Represents an instance of AddressService class
     */
    private final AddressService addressService;

    /**
     * Represents an instance of IdentityDocumentService class
     */
    private final IdentityDocumentService identityDocumentService;

    /**
     * Represents an instance of PersonalDataService class
     */
    private final PersonalDataService personalDataService;
}
