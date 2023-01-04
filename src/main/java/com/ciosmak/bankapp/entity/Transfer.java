package com.ciosmak.bankapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true)

@Entity
@Table(name = "transfers")
public class Transfer extends AbstractEntity
{
    @ToString.Include
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "amount_of_money", scale = 2, nullable = false)
    private BigDecimal amountOfMoney;

    @ToString.Include
    @Column(name = "receiving_bank_account_number", length = 26, nullable = false)
    private String receivingBankAccountNumber;

    @ToString.Include
    @Column(name = "execution_date", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime executionDate;

    @ToString.Include
    @Column(name = "posting_date", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime postingDate;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "transfers_bank_accounts", joinColumns = {@JoinColumn(name = "transfer_id")}, inverseJoinColumns = {@JoinColumn(name = "bank_account_id")})
    private List<BankAccount> bankAccounts = new ArrayList<>();

}
