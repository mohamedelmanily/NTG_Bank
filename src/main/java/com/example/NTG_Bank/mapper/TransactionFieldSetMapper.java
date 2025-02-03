//package com.example.NTG_Bank.mapper;
//import com.example.NTG_Bank.entity.Transaction;
//import org.springframework.batch.item.file.mapping.FieldSetMapper;
//import org.springframework.batch.item.file.transform.FieldSet;
//import org.springframework.validation.BindException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeParseException;
//import java.util.logging.Logger;
//
//public class TransactionFieldSetMapper implements FieldSetMapper<Transaction> {
//
//    private static final Logger LOGGER = Logger.getLogger(TransactionFieldSetMapper.class.getName());
//    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//    @Override
//    public Transaction mapFieldSet(FieldSet fieldSet) throws BindException {
//        Transaction transaction = new Transaction();
//
//        // Map fields from CSV to Transaction entity
//        transaction.setTransactionId(fieldSet.readLong("transactionId"));
//        transaction.setDescription(fieldSet.readString("description"));
//        transaction.setCredit(fieldSet.readDouble("credit"));
//        transaction.setDebit(fieldSet.readDouble("debit"));
//
//        // Handle timestamp field safely
//        String timestampStr = fieldSet.readString("timestamp").replace("'", "").trim();
//        if (!timestampStr.isEmpty()) {
//            try {
////                transaction.setTimestamp(LocalDateTime.parse(timestampStr, FORMATTER));
//            } catch (DateTimeParseException e) {
//                LOGGER.warning("⛔ Invalid timestamp format: " + timestampStr);
//            }
//        }
//
//        return transaction;
//    }
//}
