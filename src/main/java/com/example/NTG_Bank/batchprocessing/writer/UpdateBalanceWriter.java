package com.example.NTG_Bank.batchprocessing.writer;

import com.example.NTG_Bank.entity.Account;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class UpdateBalanceWriter {

    public JdbcBatchItemWriter<Account> updateBalanceWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Account>()
                .sql("UPDATE account SET current_balance = :currentBalance WHERE account_id = :accountId")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }
}

