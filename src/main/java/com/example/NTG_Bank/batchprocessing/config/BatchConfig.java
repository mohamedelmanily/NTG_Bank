package com.example.NTG_Bank.batchprocessing.config;

import com.example.NTG_Bank.batchprocessing.processor.AccountItemProcessor;
import com.example.NTG_Bank.batchprocessing.processor.CustomerItemProcessor;
import com.example.NTG_Bank.batchprocessing.processor.TransactionItemProcessor;
import com.example.NTG_Bank.batchprocessing.reader.AccountItemReader;
import com.example.NTG_Bank.batchprocessing.reader.CustomerItemReader;
import com.example.NTG_Bank.batchprocessing.reader.TransactionsItemReader;
import com.example.NTG_Bank.batchprocessing.writer.AccountItemWriter;
import com.example.NTG_Bank.batchprocessing.writer.CustomerItemWriter;
import com.example.NTG_Bank.batchprocessing.writer.TransactionItemWriter;
import com.example.NTG_Bank.entity.Account;
import com.example.NTG_Bank.entity.Customer;
import com.example.NTG_Bank.entity.Transaction;
//import com.example.NTG_Bank.mapper.TransactionMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
//    @Bean
//    public TransactionMapper transactionMapper(){
//        return new TransactionMapper();
//    }

    //  Readers
    @Bean
    public FlatFileItemReader<Customer> customerItemReader() {
        return new CustomerItemReader().reader();
    }

    @Bean
    public FlatFileItemReader<Transaction> transactionItemReader() {
        return new TransactionsItemReader().reader();
    }

    @Bean
    public FlatFileItemReader<Account> accountItemReader() {
        return new AccountItemReader().reader();
    }

    //    Processors
    @Bean
    public CustomerItemProcessor customerItemProcessor() {
        return new CustomerItemProcessor();
    }

    @Bean
    public TransactionItemProcessor transactionItemProcessor() {
        return new TransactionItemProcessor();
    }

    @Bean
    public AccountItemProcessor accountItemProcessor() {
        return new AccountItemProcessor();
    }

    //  Writers
    @Bean
    public JdbcBatchItemWriter<Customer> customerItemWriter(DataSource dataSource) {
        return new CustomerItemWriter().writer(dataSource);
    }

    @Bean
    public JdbcBatchItemWriter<Transaction> transactionItemWriter(DataSource dataSource) {
        return new TransactionItemWriter().writer(dataSource);
    }

    @Bean
    public JdbcBatchItemWriter<Account> accountItemWriter(DataSource dataSource) {
        return new AccountItemWriter().writer(dataSource);
    }

    //    Steps
    @Bean
    public Step transactionStep(JobRepository jobRepository, DataSourceTransactionManager transactionManager, FlatFileItemReader<Transaction> transactionItemReader, TransactionItemProcessor transactionItemProcessor, JdbcBatchItemWriter<Transaction> transactionItemWriter) {
        return new StepBuilder("transactionStep", jobRepository).<Transaction, Transaction>chunk(2, transactionManager).reader(transactionItemReader).processor(transactionItemProcessor).writer(transactionItemWriter)
//                .faultTolerant()
//                .skip(FlatFileParseException.class)
//                .skip(IllegalArgumentException.class) // To skip records with bad date format
//                .skipLimit(10).listener(new SkipListener<Transaction, Transaction>() {
//            @Override
//            public void onSkipInRead(Throwable t) {
//                System.err.println("خطأ في قراءة البيانات: " + t.getMessage());
//            }
//        })
                .build();
    }

    @Bean
    public Step customerStep(JobRepository jobRepository, DataSourceTransactionManager transactionManager, FlatFileItemReader<Customer> customerItemReader, CustomerItemProcessor customerItemProcessor, JdbcBatchItemWriter<Customer> customerItemWriter) {
        return new StepBuilder("customerStep", jobRepository).<Customer, Customer>chunk(2, transactionManager).reader(customerItemReader).processor(customerItemProcessor).writer(customerItemWriter).build();
    }

    @Bean
    public Step accountStep(JobRepository jobRepository, DataSourceTransactionManager transactionManager, FlatFileItemReader<Account> accountItemReader, AccountItemProcessor accountItemProcessor, JdbcBatchItemWriter<Account> accountItemWriter) {
        return new StepBuilder("accountStep", jobRepository).<Account, Account>chunk(2, transactionManager).reader(accountItemReader).processor(accountItemProcessor).writer(accountItemWriter).build();
    }

    @Bean
    public Job importDataJob(JobRepository jobRepository, Step customerStep, Step accountStep, Step transactionStep) {
        return new JobBuilder("importDataJob", jobRepository).start(customerStep).next(accountStep).next(transactionStep).build();
    }
}

