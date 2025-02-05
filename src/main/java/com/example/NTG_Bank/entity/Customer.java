package com.example.NTG_Bank.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Customer {
    @Id
    private Long customerId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String postalCode;
    private String emailAddress;
    private String homePhone;
    private String cellPhone;
    private String workPhone;

//Relation from Customer to Account
    @OneToMany(mappedBy = "customer" ,cascade = CascadeType.REFRESH, orphanRemoval = true)
    private List<Account> accounts;


}
