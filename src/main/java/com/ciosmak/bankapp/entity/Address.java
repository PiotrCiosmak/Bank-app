package com.ciosmak.bankapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true)

@Entity
@Table(name = "addresses")
public class Address extends AbstractEntity
{
    @ToString.Include
    @Column(name = "street", nullable = false)
    private String street;

    @ToString.Include
    @Column(name = "house_number", nullable = false)
    private String houseNumber;

    @ToString.Include
    @Column(name = "apartment_number")
    private String apartmentNumber;

    @ToString.Include
    @Column(name = "post_code", length = 6, nullable = false)
    private String postCode;

    @ToString.Include
    @Column(name = "town", nullable = false)
    private String town;

    @ToString.Include
    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "is_mailing", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isMailing;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "addresses_users", joinColumns = {@JoinColumn(name = "address_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> users = new ArrayList<>();
}
