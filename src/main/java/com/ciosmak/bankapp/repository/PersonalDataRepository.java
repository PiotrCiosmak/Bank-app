package com.ciosmak.bankapp.repository;

import com.ciosmak.bankapp.entity.PersonalData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalDataRepository extends JpaRepository<PersonalData, Long>
{
}
