package com.example.NTG_Bank.batchprocessing.config;
import com.example.NTG_Bank.batchprocessing.processor.*;
import com.example.NTG_Bank.batchprocessing.reader.AccountItemReader;
import com.example.NTG_Bank.batchprocessing.reader.CustomerItemReader;
import com.example.NTG_Bank.batchprocessing.reader.StatementReader;
import com.example.NTG_Bank.batchprocessing.reader.TransactionsItemReader;
import com.example.NTG_Bank.batchprocessing.writer.*;
import com.example.NTG_Bank.data_structure.Statement;
import com.example.NTG_Bank.entity.*;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import javax.sql.DataSource;

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
    private final UpdateBalanceProcessor updateBalanceProcessor;
    private final UpdateBalanceWriter updateBalanceWriter;
    private DataSource dataSource;
    @Bean
    public DataSourceTransactionManager transactionManagerBean(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


    //====================  Readers ===============================
//    @Bean
//    public FlatFileItemReader<Customer> customerItemReaderBean() {
//        return customerItemReader.reader();
//    }


//    @Bean
//    public FlatFileItemReader<Transaction> transactionItemReaderBean() {
//        return  transactionsItemReader.reader();
//    }

//    @Bean
//    public FlatFileItemReader<Account> accountItemReaderBean() {
//        return accountItemReader.reader();
//    }

//    @Bean
//    public JdbcCursorItemReader<Customer> statementReaderBean(DataSource dataSource) {
//        return statementReader.statementReader(dataSource);
//    }


    //==============================    Processors ================================
//    @Bean
//    public CustomerItemProcessor customerItemProcessorBean() {
//        return customerItemProcessor;
//    }

//    @Bean
//    public TransactionItemProcessor transactionItemProcessorBean() {
//        return transactionItemProcessor;
//    }
//
//    @Bean
//    public AccountItemProcessor accountItemProcessorBean() {
//        return accountItemProcessor;
//    }
//
//    @Bean
//    public ItemProcessor<Customer, Statement> statementProcessorBean(JdbcTemplate jdbcTemplate) {
//        return statementProcessor.statementProcessor(jdbcTemplate);
//    }
//
//
//    @Bean
//    public ItemProcessor<Account, Account> updateBalanceProcessorBean() {
//        return updateBalanceProcessor;
//    }
    //===================================  Writers =============================
    @Bean
    public JdbcBatchItemWriter<Customer> customerItemWriterBean(DataSource dataSource) {
        return customerItemWriter.writer(dataSource);
    }

    @Bean
    public JdbcBatchItemWriter<Transaction> transactionItemWriterBean(DataSource dataSource) {
        return transactionItemWriter.writer(dataSource);
    }
//
    @Bean
    public JdbcBatchItemWriter<Account> accountItemWriterBean(DataSource dataSource) {
        return accountItemWriter.writer(dataSource);
    }
    @Bean
    public ItemWriter<Statement> statementItemWriterBean() {
        return statementWriter.statementWriter();
    }
    @Bean
    public ItemWriter<Account> updateBalanceWriterBean(DataSource dataSource){
        return updateBalanceWriter.updateBalanceWriter(dataSource);
    }
    //===================================    Steps ====================================

    @Bean
    public Step customerStep(JobRepository jobRepository,
                            DataSource dataSource) {
        return new StepBuilder("customerStep", jobRepository)
                .<Customer, Customer>chunk(2, transactionManagerBean(dataSource))
                .reader(customerItemReader.reader())
                .processor(customerItemProcessor)
                .writer(customerItemWriterBean(dataSource))
                .build();
    }

    @Bean
    public Step accountStep(JobRepository jobRepository,
                            DataSource dataSource) {
        return new StepBuilder("accountStep", jobRepository)
                .<Account, Account>chunk(2, transactionManagerBean(dataSource))
                .reader(accountItemReader.reader())
                .processor(accountItemProcessor)
                .writer(accountItemWriterBean(dataSource))
                .build();
    }
    @Bean
    public Step transactionStep(JobRepository jobRepository,
                                DataSource dataSource ) {
        return new StepBuilder("transactionStep", jobRepository)
                .<Transaction, Transaction>chunk(2, transactionManagerBean(dataSource))
                .reader(transactionsItemReader.reader())
                .processor(transactionItemProcessor)
                .writer(transactionItemWriterBean(dataSource))
                .build();
    }


    @Bean
    public Step generateStatementsStep(JobRepository jobRepository,
                                       JdbcTemplate jdbcTemplate,
                                       DataSource dataSource) {
        return new StepBuilder("generateStatementsStep", jobRepository)
                .<Customer, Statement>chunk(10, transactionManagerBean(dataSource))
                .reader(statementReader.statementReader(dataSource))
                .processor(statementProcessor.statementProcessor(jdbcTemplate))
                .writer(statementItemWriterBean())
                .build();
    }
    @Bean
    public Step updateBalanceStep(JobRepository jobRepository,
                                  DataSource dataSource) {
        return new StepBuilder("updateBalanceStep",jobRepository)
                .<Account, Account>chunk(10,transactionManagerBean(dataSource))
                .reader(accountItemReader.reader())
                .processor(updateBalanceProcessor)
                .writer(updateBalanceWriterBean(dataSource))
                .build();
    }

    @Bean
    public Job importDataJob(JobRepository jobRepository,
                             Step customerStep,
                             Step accountStep,
                             Step transactionStep,
                             Step updateBalanceStep,
                             Step generateStatementsStep) {
        return new JobBuilder("importDataJob", jobRepository)
                .start(customerStep)
                .next(accountStep)
                .next(transactionStep)
                .next(updateBalanceStep)
                .next(generateStatementsStep)
                .build();
    }

}

