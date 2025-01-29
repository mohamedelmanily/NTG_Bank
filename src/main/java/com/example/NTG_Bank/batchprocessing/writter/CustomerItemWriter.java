package com.example.NTG_Bank.batchprocessing.writter;
import com.example.NTG_Bank.entity.Customer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import javax.sql.DataSource;

public class CustomerItemWriter {

    public JdbcBatchItemWriter<Customer> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Customer>()
                .sql("INSERT INTO customer (id, first_name, middle_name, last_name, address1, address2, city, state, postal_code, email_address, home_phone, cell_phone, work_phone) " +
                        "VALUES (:id, :firstName, :middleName, :lastName, :address1, :address2, :city, :state, :postalCode, :emailAddress, :homePhone, :cellPhone, :workPhone)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }
}
