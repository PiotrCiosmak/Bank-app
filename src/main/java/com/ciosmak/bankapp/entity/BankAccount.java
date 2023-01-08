package com.ciosmak.bankapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true)

@Entity
@Table(name = "bank_accounts")
public class BankAccount extends AbstractEntity
{
    @Column(name = "balance", scale = 2, nullable = false)
    private BigDecimal balance;

    @ToString.Include
    @Column(name = "bank_account_number", length = 26, nullable = false, unique = true)
    private String bankAccountNumber;

    @ToString.Include
    @Column(name = "international_bank_account_number", length = 34, nullable = false, unique = true)
    private String internationalBankAccountNumber;

    @ToString.Include
    @Column(name = "name", nullable = false)
    private String name;

    @ToString.Include
    @Column(name = "bank_identification_code", length = 11, nullable = false)
    private String bankIdentificationCode;

    @Column(name = "is_open", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean isOpen;

    @Column(name = "maintenance_fee", scale = 2, nullable = false)
    private BigDecimal maintenanceFee;

    @Column(name = "interest", scale = 2)
    private BigDecimal interest;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "payment_card_id")
    private PaymentCard paymentCard;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "transfers_bank_accounts", joinColumns = {@JoinColumn(name = "bank_account_id")}, inverseJoinColumns = {@JoinColumn(name = "transfer_id")})
    private List<Transfer> transfers = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private User user;
}
