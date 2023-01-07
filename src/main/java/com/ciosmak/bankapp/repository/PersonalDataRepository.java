package com.ciosmak.bankapp.repository;

import com.ciosmak.bankapp.entity.PersonalData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonalDataRepository extends JpaRepository<PersonalData, Long>
{
    Optional<PersonalData> findByPersonalIdentityNumber(String identityNumber);
}
