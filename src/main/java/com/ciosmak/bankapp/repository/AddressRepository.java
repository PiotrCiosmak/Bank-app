package com.ciosmak.bankapp.repository;

import com.ciosmak.bankapp.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The AddressRepository interface is a Spring Data JPA repository for {@link Address} entities.
 * It extends the {@link JpaRepository} interface and provides methods for common CRUD operations on addresses.
 * This interface also enables the use of Spring Data specific methods for querying the database.
 *
 * @author Author Piotr Ciosmak
 * @version 1.0
 * @see JpaRepository
 * @see Address
 */
public interface AddressRepository extends JpaRepository<Address, Long>
{
}
