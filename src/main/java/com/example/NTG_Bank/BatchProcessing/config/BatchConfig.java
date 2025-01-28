package com.example.NTG_Bank.BatchProcessing.config;

import com.example.NTG_Bank.BatchProcessing.processor.CustomerItemProcessor;
import com.example.NTG_Bank.BatchProcessing.reader.CustomerItemReader;
import com.example.NTG_Bank.BatchProcessing.writter.CustomerItemWriter;
import com.example.NTG_Bank.entity.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
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

    @Bean
    public FlatFileItemReader<Customer> customerItemReader() {
        return new CustomerItemReader().reader();
    }

    @Bean
    public CustomerItemProcessor customerItemProcessor() {
        return new CustomerItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Customer> customerItemWriter(DataSource dataSource) {
        return new CustomerItemWriter().writer(dataSource);
    }

    @Bean
    public Step customerStep(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
                             FlatFileItemReader<Customer> customerItemReader,
                             CustomerItemProcessor customerItemProcessor,
                             JdbcBatchItemWriter<Customer> customerItemWriter) {
        return new StepBuilder("customerStep", jobRepository)
                .<Customer, Customer>chunk(2, transactionManager)
                .reader(customerItemReader)
                .processor(customerItemProcessor)
                .writer(customerItemWriter)
                .build();
    }

    @Bean
    public Job importCustomerJob(JobRepository jobRepository, Step customerStep) {
        return new JobBuilder("importCustomerJob", jobRepository)
                .start(customerStep)
                .build();
    }
}

