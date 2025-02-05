package com.example.NTG_Bank.batchprocessing.writer;

import com.example.NTG_Bank.entity.Transaction;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
@Component
public class TransactionItemWriter {
    public JdbcBatchItemWriter<Transaction> writer(DataSource dataSource){
        return new JdbcBatchItemWriterBuilder<Transaction>()
                .sql("INSERT INTO transaction (transaction_id, account_id, description, credit, debit,timestamp) " +
                        "VALUES (:transactionId, :accountId, :description, :credit, :debit,:timestamp)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }
}
