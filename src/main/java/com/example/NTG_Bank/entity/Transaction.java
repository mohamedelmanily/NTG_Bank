package com.example.NTG_Bank.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Transaction {
    @Id
    private Long transactionId;
    @Transient
    private Long accountId;
    private String description;
    private Double credit;
    private Double debit;
    private LocalDateTime timestamp;
//    Relationship between 'Transaction' and 'Account'
    @ManyToOne
    @JoinColumn(name="accountId")
    private Account account;
}
