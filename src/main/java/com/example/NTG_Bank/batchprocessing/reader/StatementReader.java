package com.example.NTG_Bank.batchprocessing.reader;

import com.example.NTG_Bank.entity.Customer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.logging.Logger;

@Component
public class StatementReader {

    public JdbcCursorItemReader<Customer> statementReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Customer>()
                .dataSource(dataSource)
                .sql("SELECT * FROM customer")
                .rowMapper(new BeanPropertyRowMapper<>(Customer.class))
                .name("statementReader")
                .build();
    }


}

