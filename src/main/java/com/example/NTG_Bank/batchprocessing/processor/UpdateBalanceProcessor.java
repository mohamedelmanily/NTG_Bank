package com.example.NTG_Bank.batchprocessing.processor;

import com.example.NTG_Bank.entity.Account;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UpdateBalanceProcessor implements ItemProcessor<Account, Account> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Account process(Account account) throws Exception {
        Double currentBalance = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(credit), 0) - COALESCE(SUM(debit), 0) " +
                        "FROM transaction WHERE account_id = ?",
                Double.class,
                account.getAccountId()
        );

        account.setCurrentBalance(currentBalance != null ? currentBalance : 0.0);
        return account;
    }
}

