package com.example.NTG_Bank.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Account {
    @Id
    private Long accountId;
    @Transient
    private Long customerId;
    private Double currentBalance;
    private LocalDateTime lastStatementDate;


//    Relation between Account to Customer
    @ManyToOne()
    @JoinColumn(name="customerId")
    private Customer customer;

//    Relation between Account to Transaction
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;

}
