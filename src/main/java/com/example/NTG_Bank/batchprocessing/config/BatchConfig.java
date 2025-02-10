package com.example.NTG_Bank.batchprocessing.config;

import com.example.NTG_Bank.batchprocessing.processor.AccountItemProcessor;
import com.example.NTG_Bank.batchprocessing.processor.CustomerItemProcessor;
//import com.example.NTG_Bank.batchprocessing.processor.StatementProcessor;
import com.example.NTG_Bank.batchprocessing.processor.StatementProcessor;
import com.example.NTG_Bank.batchprocessing.processor.TransactionItemProcessor;
import com.example.NTG_Bank.batchprocessing.reader.AccountItemReader;
import com.example.NTG_Bank.batchprocessing.reader.CustomerItemReader;
//import com.example.NTG_Bank.batchprocessing.reader.StatementReader;
import com.example.NTG_Bank.batchprocessing.reader.StatementReader;
import com.example.NTG_Bank.batchprocessing.reader.TransactionsItemReader;
import com.example.NTG_Bank.batchprocessing.writer.AccountItemWriter;
import com.example.NTG_Bank.batchprocessing.writer.CustomerItemWriter;
import com.example.NTG_Bank.batchprocessing.writer.StatementWriter;
import com.example.NTG_Bank.batchprocessing.writer.TransactionItemWriter;
import com.example.NTG_Bank.data_structure.AccountSummary;
import com.example.NTG_Bank.data_structure.Statement;
import com.example.NTG_Bank.entity.*;
//import com.example.NTG_Bank.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Configuration
public class BatchConfig {
    private final CustomerItemReader customerItemReader;
    private final StatementReader statementReader;
    private final TransactionsItemReader transactionsItemReader;
    private final AccountItemReader accountItemReader;
    private final CustomerItemProcessor customerItemProcessor;
    private final TransactionItemProcessor transactionItemProcessor;
    private final AccountItemProcessor accountItemProcessor;
    private final CustomerItemWriter customerItemWriter;
    private final TransactionItemWriter transactionItemWriter;
    private final AccountItemWriter accountItemWriter;
    private final StatementProcessor statementProcessor;
    private final StatementWriter statementWriter;
    private JdbcTemplate jdbcTemplate;

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    //====================  Readers ===============================
    @Bean
    public FlatFileItemReader<Customer> customerItemReaderBean() {
        return customerItemReader.reader();
    }


    @Bean
    public FlatFileItemReader<Transaction> transactionItemReaderBean() {
        return  transactionsItemReader.reader();
    }

    @Bean
    public FlatFileItemReader<Account> accountItemReaderBean() {
        return accountItemReader.reader();
    }

    @Bean
    public JdbcCursorItemReader<Customer> statementReaderBean(DataSource dataSource) {
        return statementReader.statementReader(dataSource);
    }
//    public JdbcCursorItemReader<Customer> statementReader(DataSource dataSource) {
//        return new JdbcCursorItemReader<Customer>() {{
//            setDataSource(dataSource);
//            setSql("SELECT * FROM customer");
//            setRowMapper(new BeanPropertyRowMapper<>(Customer.class));
//        }};
//    }

    //==============================    Processors ================================
    @Bean
    public CustomerItemProcessor customerItemProcessorBean() {
        return customerItemProcessor;
    }

    @Bean
    public TransactionItemProcessor transactionItemProcessorBean() {
        return transactionItemProcessor;
    }

    @Bean
    public AccountItemProcessor accountItemProcessorBean() {
        return accountItemProcessor;
    }

    @Bean
    public ItemProcessor<Customer, Statement> statementProcessorBean(JdbcTemplate jdbcTemplate) {
        return statementProcessor.statementProcessor(jdbcTemplate);
    }


    //===================================  Writers =============================
    @Bean
    @Transactional
    public JdbcBatchItemWriter<Customer> customerItemWriterBean(DataSource dataSource) {
        return customerItemWriter.writer(dataSource);
    }

    @Bean
    public JdbcBatchItemWriter<Transaction> transactionItemWriterBean(DataSource dataSource) {
        return transactionItemWriter.writer(dataSource);
    }

    @Bean
    public JdbcBatchItemWriter<Account> accountItemWriterBean(DataSource dataSource) {
        return accountItemWriter.writer(dataSource);
    }
    @Bean
    public ItemWriter<Statement> statementItemWriterBean(DataSource dataSource) {
        return statementWriter.statementWriter();
    }
    //===================================    Steps ====================================
    @Bean
    public Step transactionStep(JobRepository jobRepository, DataSourceTransactionManager transactionManager, FlatFileItemReader<Transaction> transactionItemReader, TransactionItemProcessor transactionItemProcessor, JdbcBatchItemWriter<Transaction> transactionItemWriter) {
        return new StepBuilder("transactionStep", jobRepository).<Transaction, Transaction>chunk(2, transactionManager).reader(transactionItemReader).processor(transactionItemProcessor).writer(transactionItemWriter)
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

//=========================================================================================

@Bean
public Step generateStatementsStep(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
                                   JdbcCursorItemReader<Customer> statementReader,
                                   ItemProcessor<Customer, Statement> statementProcessor,
                                   ItemWriter<Statement> statementWriter) {
    return new StepBuilder("generateStatementsStep", jobRepository)
            .<Customer, Statement>chunk(10, transactionManager)
            .reader(statementReader)
            .processor(statementProcessor)
            .writer(statementWriter)
            .build();
}

    @Bean

    public Job importDataJob(JobRepository jobRepository, Step customerStep, Step accountStep, Step transactionStep,Step generateStatementsStep) {
        return new JobBuilder("importDataJob", jobRepository)
                .start(customerStep).
                next(accountStep)
                .next(transactionStep)
                .next(generateStatementsStep)
                .build();
    }

}

