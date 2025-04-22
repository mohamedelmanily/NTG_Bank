package com.example.NTG_Bank.batchprocessing.writer;
import com.example.NTG_Bank.data_structure.AccountSummary;
import com.example.NTG_Bank.data_structure.Statement;
import com.example.NTG_Bank.entity.Transaction;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class StatementWriter {

    private static final String DIRECTORY_NAME = "src/main/resources/CustomersInfo";

    public ItemWriter<Statement> statementWriter() {
        return statements -> {
            File directory = new File(DIRECTORY_NAME);
            if (!directory.exists()) {
                directory.mkdir();
            }

            for (Statement statement : statements) {
                String fileName = DIRECTORY_NAME + File.separator + statement.getCustomerName() + ".txt";
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                    writer.write("========================================\n");
                    writer.write("NTG Bank Statement\n");
                    writer.write("========================================\n\n");
                    writer.write(String.format("Customer: %s\n", statement.getCustomerName()));
                    writer.write(String.format("Address: %s\n", statement.getCustomerAddress()));
                    writer.write(String.format("City: %s\n", statement.getCity()));
                    writer.write(String.format("State: %s\n", statement.getState()));
                    writer.write(String.format("PostalCode: %s\n", statement.getPostalCode()));
                    writer.write("\n========================================\n\n");
                    for (AccountSummary accountSummary : statement.getAccountSummaries()) {
                        writer.write(String.format("Account ID: %d\n", accountSummary.getAccountId()));
                        writer.write("Transactions:\n");
                        writer.write("----------------------------------------\n");
                        for (Transaction transaction : accountSummary.getTransactions()) {
                            writer.write(transaction.toString() + "\n");
                        }
                        writer.write("----------------------------------------\n");
                        writer.write(String.format("Total Credits: %.2f\n", accountSummary.getTotalCredits()));
                        writer.write(String.format("Total Debits: %.2f\n", accountSummary.getTotalDebits()));
                        writer.write(String.format("Current Balance: %.2f\n", accountSummary.getCurrentBalance()));
                        writer.write("================================================================\n\n");
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to write statement file", e);
                }
            }
        };
    }
}
