package com.ciosmak.bankapp.repository;

import com.ciosmak.bankapp.entity.IdentityDocument;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The IdentityDocumentRepository interface is a Spring Data JPA repository for {@link IdentityDocument} entities.
 * It extends the {@link JpaRepository} interface and provides methods for common CRUD operations on identity documents.
 * This interface also enables the use of Spring Data specific methods for querying the database.
 *
 * @author Author Piotr Ciosmak
 * @version 1.0
 * @see JpaRepository
 * @see IdentityDocument
 */
public interface IdentityDocumentRepository extends JpaRepository<IdentityDocument, Long>
{
}
