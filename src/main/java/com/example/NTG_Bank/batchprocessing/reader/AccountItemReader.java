package com.example.NTG_Bank.batchprocessing.reader;

import com.example.NTG_Bank.entity.Account;
import com.example.NTG_Bank.mapper.AccountFieldSetMapper;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class AccountItemReader  {
    public FlatFileItemReader<Account> reader(){
        return new FlatFileItemReaderBuilder<Account>()
                .name("accountItemReader")
                .resource(new ClassPathResource("accounts1.csv"))
                .delimited()
                .names("accountId","customerId")
                .targetType(Account.class)
                .linesToSkip(1)
                .build();

    }
}
