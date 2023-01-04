package com.ciosmak.bankapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true)

@Entity
@Table(name = "personal_data")
public class PersonalData extends AbstractEntity
{
    @ToString.Include
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @ToString.Include
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ToString.Include
    @Column(name = "phone_number", length = 12, nullable = false)
    private String phoneNumber;

    @ToString.Include
    @Column(name = "family_name", nullable = false)
    private String familyName;

    @ToString.Include
    @Column(name = "personal_identity_number", length = 11, nullable = false, unique = true)
    private String personalIdentityNumber;

    @ToString.Include
    @Column(name = "birth_place", nullable = false)
    private String birthPlace;

    @ToString.Include
    @Column(name = "nationality", nullable = false)
    private String nationality;

    @ToString.Include
    @Column(name = "mothers_name", nullable = false)
    private String mothersName;

    @ToString.Include
    @Column(name = "mothers_maiden_name", nullable = false)
    private String mothersMaidenName;

    @OneToOne(mappedBy = "personalData", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private User user;
}
