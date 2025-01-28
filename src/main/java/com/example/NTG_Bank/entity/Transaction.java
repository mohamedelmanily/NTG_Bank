package com.example.NTG_Bank.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Transaction {
    @Id
    private Long id;
    private String description;
    private Double creditAmount;
    private Double debitAmount;
    private LocalDateTime timestamp;

//    Relationship between 'Transaction' and 'Account'
    @ManyToOne
    @JoinColumn(name="account_id")
    private Account account;
}
