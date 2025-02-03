package com.example.NTG_Bank.batchprocessing.writer;
import com.example.NTG_Bank.entity.Account;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import javax.sql.DataSource;

public class AccountItemWriter {
    public JdbcBatchItemWriter<Account> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Account>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO account (account_id,customer_id) " +
                        "VALUES (:accountId,:customerId)")
                .dataSource(dataSource)
                .build();
    }
}
