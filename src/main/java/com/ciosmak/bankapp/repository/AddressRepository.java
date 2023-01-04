package com.ciosmak.bankapp.repository;

import com.ciosmak.bankapp.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long>
{
}
