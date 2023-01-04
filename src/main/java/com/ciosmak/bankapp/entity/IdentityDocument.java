package com.ciosmak.bankapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true)

@Entity
@Table(name = "identity_documents")
public class IdentityDocument extends AbstractEntity
{
    @ToString.Include
    @Column(name = "release_date", columnDefinition = "DATE", nullable = false)
    private LocalDate releaseDate;

    @ToString.Include
    @Column(name = "expiry_date", columnDefinition = "DATE", nullable = false)
    private LocalDate expiryDate;

    @ToString.Include
    @Column(name = "series_and_number", length = 9, nullable = false)
    private String seriesAndNumber;

    @OneToOne(mappedBy = "identityDocument", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private User user;
}
