package com.example.NTG_Bank.data_structure;

import com.example.NTG_Bank.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountSummary {
    private Long accountId;
    private List<Transaction> transactions;
    private double totalCredits;
    private double totalDebits;
    private double currentBalance;
    // Getters and Setters
}