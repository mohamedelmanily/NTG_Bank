package com.example.NTG_Bank.batchprocessing.writer;
import com.example.NTG_Bank.entity.Customer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;

@Component
public class CustomerItemWriter {

    public JdbcBatchItemWriter<Customer> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Customer>()
                .sql("INSERT INTO customer (customer_id, first_name, middle_name, last_name, address1, address2, city, state, postal_code, email_address, home_phone, cell_phone, work_phone) " +
                        "VALUES (:customerId, :firstName, :middleName, :lastName, :address1, :address2, :city, :state, :postalCode, :emailAddress, :homePhone, :cellPhone, :workPhone) " +
                        "ON CONFLICT (customer_id) DO UPDATE " +
                        "SET first_name = EXCLUDED.first_name, " +
                        "middle_name = EXCLUDED.middle_name, " +
                        "last_name = EXCLUDED.last_name, " +
                        "address1 = EXCLUDED.address1, " +
                        "address2 = EXCLUDED.address2, " +
                        "city = EXCLUDED.city, " +
                        "state = EXCLUDED.state, " +
                        "postal_code = EXCLUDED.postal_code, " +
                        "email_address = EXCLUDED.email_address, " +
                        "home_phone = EXCLUDED.home_phone, " +
                        "cell_phone = EXCLUDED.cell_phone, " +
                        "work_phone = EXCLUDED.work_phone")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

}

