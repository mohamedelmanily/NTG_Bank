package com.example.NTG_Bank.BatchProcessing.reader;
import com.example.NTG_Bank.entity.Customer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.core.io.ClassPathResource;

public class CustomerItemReader {

    public FlatFileItemReader<Customer> reader() {
        return new FlatFileItemReaderBuilder<Customer>()
                .name("customerItemReader")
                .resource(new ClassPathResource("customers.csv"))
                .linesToSkip(1)
                .delimited()
                .names("id", "firstName", "middleName", "lastName", "address1", "address2", "city", "state", "postalCode", "emailAddress", "homePhone", "cellPhone", "workPhone")
                .targetType(Customer.class)
                .build();
    }
}

