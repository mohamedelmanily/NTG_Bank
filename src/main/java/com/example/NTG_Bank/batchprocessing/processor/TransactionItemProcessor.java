package com.example.NTG_Bank.batchprocessing.processor;

import com.example.NTG_Bank.entity.Transaction;
import org.springframework.batch.item.ItemProcessor;

public class TransactionItemProcessor implements ItemProcessor<Transaction, Transaction> {
    @Override
    public Transaction process(Transaction transaction) throws Exception {
        if(transaction.getDescription() == null) {
            transaction.setDescription("Unknown");
        }
        return transaction;
    }
}
