package com.example.NTG_Bank.batchprocessing.reader;
import com.example.NTG_Bank.entity.Transaction;
//import com.example.NTG_Bank.mapper.TransactionFieldSetMapper;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class TransactionsItemReader {
    public FlatFileItemReader<Transaction> reader() {
        BeanWrapperFieldSetMapper<Transaction> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Transaction.class);

        // تحديد محرر مخصص لتحليل LocalDateTime
        fieldSetMapper.setCustomEditors(Map.of(
                LocalDateTime.class, new PropertyEditorSupport() {
                    @Override
                    public void setAsText(String text) {
                        if (text != null && text.startsWith("'") && text.endsWith("'")) {
                            text = text.substring(1, text.length() - 1);
                        }
                        if (text != null && !text.isEmpty()) {
                            setValue(LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                        } else {
                            setValue(null);
                        }
                    }
                }
        ));

        return new FlatFileItemReaderBuilder<Transaction>()
                .name("transactionsItemReader")
                .resource(new ClassPathResource("transactions.csv"))
                .delimited()
                .names("transactionId", "accountId", "description", "credit", "debit", "timestamp") // تأكد من تطابق الأسماء مع Transaction
                .linesToSkip(1)
                .fieldSetMapper(fieldSetMapper) // استخدام FieldSetMapper
                .build();
    }

//public FlatFileItemReader<Transaction> reader() {
//    FlatFileItemReader<Transaction> reader = new FlatFileItemReader<>();
//    reader.setResource(new ClassPathResource("transactions1.csv"));
//    DefaultLineMapper<Transaction> lineMapper = new DefaultLineMapper<>();
//    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
//    tokenizer.setNames("transactionId", "accountId", "description", "credit", "debit", "timo");
//    /**
//     * *Custom converter as Spring Batch could not automatically parse it
//     */
//    BeanWrapperFieldSetMapper<Transaction> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
//    fieldSetMapper.setTargetType(Transaction.class);
//    // Register a custom editor for LocalDateTime conversion
//    fieldSetMapper.setCustomEditors(Map.of(
//            LocalDateTime.class, new PropertyEditorSupport() {
//                @Override
//                public void setAsText(String text) {
//                    if (text != null && text.startsWith("'") && text.endsWith("'")) {
//                        text = text.substring(1, text.length() - 1);  // Remove single quotes
//                    }
//                    if (text != null && !text.isEmpty()) {
//                        setValue(LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//                    } else {
//                        setValue(null); // Set null for empty string
//                    }
//                }
//            }
//    ));
//    lineMapper.setLineTokenizer(tokenizer);
//    lineMapper.setFieldSetMapper(fieldSetMapper);
//    reader.setLineMapper(lineMapper);
//
//    return reader;
//}
}