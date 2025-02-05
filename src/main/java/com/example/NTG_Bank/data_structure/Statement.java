package com.example.NTG_Bank.data_structure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Statement {
    private Long customerId;
    private String customerName;
    private String customerAddress;
    private String city;
    private String state;
    private String postalCode;
    private List<AccountSummary> accountSummaries;

    // Getters and Setters
}