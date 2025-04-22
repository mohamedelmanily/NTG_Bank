package com.example.NTG_Bank.batchprocessing.processor;
import com.example.NTG_Bank.data_structure.AccountSummary;
import com.example.NTG_Bank.data_structure.Statement;
import com.example.NTG_Bank.entity.Account;
import com.example.NTG_Bank.entity.Customer;
import com.example.NTG_Bank.entity.Transaction;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class StatementProcessor {

      public ItemProcessor<Customer, Statement> statementProcessor(JdbcTemplate jdbcTemplate) {
        return customer -> {
            Statement statement = new Statement();
            statement.setCustomerId(customer.getCustomerId());
            statement.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
            statement.setCustomerAddress(customer.getAddress1());
            statement.setPostalCode(customer.getPostalCode());
            statement.setCity(customer.getCity());
            statement.setState(customer.getState());

            // Get All Accounts that belong to this customer
            List<Account> accounts = jdbcTemplate.query(
                    "SELECT * FROM account WHERE customer_id = ?",
                    new BeanPropertyRowMapper<>(Account.class),
                    customer.getCustomerId()
            );
            //  make summary for every account
            List<AccountSummary> accountSummaries = new ArrayList<>();
            for (Account account : accounts) {
                AccountSummary accountSummary = new AccountSummary();
                accountSummary.setAccountId(account.getAccountId());

                // Get Current Balance
                Double currentBalance = jdbcTemplate.queryForObject(
                        "SELECT COALESCE(SUM(credit), 0) - COALESCE(SUM(debit), 0) FROM transaction WHERE account_id = ?",
                        Double.class,
                        account.getAccountId()
                );
                accountSummary.setCurrentBalance(Optional.ofNullable(currentBalance).orElse(0.0));

                List<Transaction> transactions = jdbcTemplate.query(
                        "SELECT * FROM transaction WHERE account_id = ?",
                        new BeanPropertyRowMapper<>(Transaction.class),
                        account.getAccountId()
                );

                if (!transactions.isEmpty()) {
                    double totalCredits = transactions.stream()
                            .filter(t -> t.getCredit() != null)
                            .mapToDouble(Transaction::getCredit)
                            .sum();

                    double totalDebits = transactions.stream()
                            .filter(t -> t.getDebit() != null)
                            .mapToDouble(Transaction::getDebit)
                            .sum();

                    accountSummary.setTransactions(transactions);
                    accountSummary.setTotalCredits(totalCredits);
                    accountSummary.setTotalDebits(totalDebits);
                } else {
                    accountSummary.setTotalCredits(0.0);
                    accountSummary.setTotalDebits(0.0);
                    accountSummary.setTransactions(new ArrayList<>());
                }

                accountSummaries.add(accountSummary);
            }

            statement.setAccountSummaries(accountSummaries);
            return statement;
        };
    }
}
