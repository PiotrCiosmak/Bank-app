package com.ciosmak.bankapp.repository;

import com.ciosmak.bankapp.entity.PersonalData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The PersonalDataRepository interface is a Spring Data JPA repository for {@link PersonalData} entities.
 * It extends the {@link JpaRepository} interface and provides methods for common CRUD operations on identity documents.
 * This interface also enables the use of Spring Data specific methods for querying the database.
 *
 * @author Author Piotr Ciosmak
 * @version 1.0
 * @see JpaRepository
 * @see PersonalData
 */
public interface PersonalDataRepository extends JpaRepository<PersonalData, Long>
{
    /**
     * Method to find PersonalData by personal identity number.
     *
     * @param identityNumber Personal identity number.
     * @return Optional of PersonalData.
     */
    Optional<PersonalData> findByPersonalIdentityNumber(String identityNumber);
}
