package com.example.NTG_Bank.repository;

import com.example.NTG_Bank.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
