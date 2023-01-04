package com.ciosmak.bankapp.repository;

import com.ciosmak.bankapp.entity.IdentityDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentityDocumentRepository extends JpaRepository<IdentityDocument, Long>
{
}
