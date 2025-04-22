package com.example.NTG_Bank.batchprocessing.writer;
import com.example.NTG_Bank.entity.Account;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
@Component
public class AccountItemWriter {

    public JdbcBatchItemWriter<Account> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Account>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO account (account_id, customer_id) " +
                        "VALUES (:accountId, :customerId) " +
                        "ON CONFLICT (account_id) DO UPDATE " +
                        "SET customer_id = EXCLUDED.customer_id")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

}

