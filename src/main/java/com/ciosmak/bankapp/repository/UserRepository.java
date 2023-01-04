package com.ciosmak.bankapp.repository;

import com.ciosmak.bankapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>
{
}
