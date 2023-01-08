package com.ciosmak.bankapp.entity;

import com.ciosmak.bankapp.payment.card.status.PaymentCardStatus;
import com.ciosmak.bankapp.payment.card.status.converter.PaymentCardStatusConverter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@ToString(onlyExplicitlyIncluded = true)

@Entity
@Table(name = "payment_cards")
public class PaymentCard extends AbstractEntity
{
    @ToString.Include
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @ToString.Include
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ToString.Include
    @Column(name = "expiry_date", columnDefinition = "DATE", nullable = false)
    private LocalDate expiryDate;

    @ToString.Include
    @Convert(converter = PaymentCardStatusConverter.class)
    @Column(name = "status", nullable = false)
    private PaymentCardStatus status;

    @ToString.Include
    @Column(name = "pin", nullable = false)
    private String pin;

    @ToString.Include
    @Column(name = "card_number", length = 16, nullable = false, unique = true)
    private String cardNumber;

    @ToString.Include
    @Column(name = "verification_value", nullable = false)
    private String verificationValue;

    @Column(name = "payment_limit_per_day", scale = 2, nullable = false)
    private BigDecimal paymentLimitPerDay;

    @Column(name = "withdraw_limit_per_day", scale = 2, nullable = false)
    private BigDecimal withdrawLimitPerDay;

    @Column(name = "internet_transaction_limit_per_day", scale = 2, nullable = false)
    private BigDecimal internetTransactionLimitPerDay;

    @Column(name = "contact_less_transactions_are_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean contactlessTransactionsAreActive;

    @Column(name = "magnetic_strip_is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean magneticStripIsActive;

    @Column(name = "transactions_with_ddc_service_are_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean transactionsWithDdcServiceAreActive;

    @Column(name = "surcharge_transactions_are_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean surchargeTransactionsAreActive;

    @Column(name = "withdrawal_fee_in_poland", scale = 2, nullable = false)
    private BigDecimal withdrawalFeeInPoland;

    @Column(name = "foreign_withdrawal_fee", scale = 2, nullable = false)
    private BigDecimal foreignWithdrawalFee;

    @Column(name = "maintenance_fee", scale = 2, nullable = false)
    private BigDecimal maintenanceFee;

    @Column(name = "minimum_number_of_transactions", nullable = false)
    private Integer minimumNumberOfTransactions;

    @Column(name = "debt_balance_is_active", nullable = false, columnDefinition = "boolean default false")
    private boolean debtBalanceIsActive;

    @Column(name = "debt_balance", scale = 2)
    private BigDecimal debtBalance;

    @Column(name = "max_debt", nullable = false, scale = 2)
    private BigDecimal maxDebt;

    @OneToOne(mappedBy = "paymentCard", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private BankAccount bankAccount;
}
