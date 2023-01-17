package com.ciosmak.bankapp.repository;

import com.ciosmak.bankapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The PersonalDataRepository interface is a Spring Data JPA repository for {@link UserRepository} entities.
 * It extends the {@link JpaRepository} interface and provides methods for common CRUD operations on identity documents.
 * This interface also enables the use of Spring Data specific methods for querying the database.
 *
 * @author Author Piotr Ciosmak
 * @version 1.0
 * @see JpaRepository
 * @see UserRepository
 */
public interface UserRepository extends JpaRepository<User, Long>
{
    /**
     * Method to find user by id.
     *
     * @param userId User id.
     * @return Optional of User.
     */
    @Override
    Optional<User> findById(Long userId);

    /**
     * Method to find user by email.
     *
     * @param email User email.
     * @return Optional of User.
     */
    Optional<User> findByEmail(String email);
}
