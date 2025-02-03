//package com.example.NTG_Bank.mapper;
//
//import com.example.NTG_Bank.entity.Account;
//import com.example.NTG_Bank.entity.Customer;
//import org.springframework.batch.item.file.mapping.FieldSetMapper;
//import org.springframework.batch.item.file.transform.FieldSet;
//import org.springframework.validation.BindException;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//
//public class AccountFieldSetMapper implements FieldSetMapper<Account> {
//    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
//
//    @Override
//    public Account mapFieldSet(FieldSet fieldSet) throws BindException {
//        Account account = new Account();
//        account.setAccountId(fieldSet.readLong("accountId"));
//        account.setCurrentBalance(fieldSet.readDouble("currentBalance"));
//
//        // Convert lastStatementDate from String to LocalDate
//        String lastStatementDateStr = fieldSet.readString("lastStatementDate");
//        account.setLastStatementDate(LocalDate.parse(lastStatementDateStr, dateFormatter));
//
//        // Handle customer mapping (assuming customerId is provided in the CSV)
//        Long customerId = fieldSet.readLong("customerId");
//        Customer customer = new Customer();
//        customer.setCustomerId(customerId);
//        account.setCustomer(customer);
//
//        return account;
//    }
//}
package com.example.NTG_Bank.mapper;

import com.example.NTG_Bank.entity.Account;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AccountFieldSetMapper implements FieldSetMapper<Account> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");

    @Override
    public Account mapFieldSet(@NonNull FieldSet fieldSet) {
        Account account = new Account();
        account.setAccountId(fieldSet.readLong("accountId"));
//        account.setCustomerId(fieldSet.readLong("customerId"));
        account.setCurrentBalance(fieldSet.readDouble("currentBalance"));

        String dateString = fieldSet.readString("lastStatementDate");
//        account.setLastStatementDate(LocalDate.parse(dateString, formatter));

        return account;
    }
}
