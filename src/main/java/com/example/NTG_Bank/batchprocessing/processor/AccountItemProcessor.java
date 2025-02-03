package com.example.NTG_Bank.batchprocessing.processor;

import com.example.NTG_Bank.entity.Account;
import org.springframework.batch.item.ItemProcessor;

public class AccountItemProcessor implements ItemProcessor<Account, Account> {
    @Override
    public Account process(Account account) throws Exception {
        if (account.getCurrentBalance() == null) {
            account.setCurrentBalance(0.0);
        }
        return account;
    }
}