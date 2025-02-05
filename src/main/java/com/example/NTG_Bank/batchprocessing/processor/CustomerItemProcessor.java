package com.example.NTG_Bank.batchprocessing.processor;
import com.example.NTG_Bank.entity.Customer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class CustomerItemProcessor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(Customer customer) throws Exception {
        if (customer.getFirstName().isEmpty()) {
            customer.setFirstName("Unknown");
        }
        return customer;
    }
}

