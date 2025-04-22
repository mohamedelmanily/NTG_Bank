package com.example.NTG_Bank.repository;

import com.example.NTG_Bank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {

}
