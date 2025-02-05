package com.example.NTG_Bank.service;

import com.example.NTG_Bank.entity.Account;
import com.example.NTG_Bank.entity.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

public class AccountService {

    public void calculateCurrentBalance(Account account) {
        if (account.getTransactions() == null || account.getTransactions().isEmpty()) {
            account.setCurrentBalance(0.0);
            return;
        }

        double balance = 0.0;
        for (Transaction transaction : account.getTransactions()) {
            if (transaction.getCredit() != null) {
                balance += transaction.getCredit();
            }
            if (transaction.getDebit() != null) {
                balance -= transaction.getDebit();
            }
        }
        account.setCurrentBalance(balance);
    }

    public void updateLastStatementDate(Account account) {
        if (account.getTransactions() == null || account.getTransactions().isEmpty()) {
            account.setLastStatementDate(null);
            return;
        }

        LocalDateTime latestDate = account.getTransactions().stream()
                .map(Transaction::getTimestamp)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        account.setLastStatementDate(latestDate);
    }
}