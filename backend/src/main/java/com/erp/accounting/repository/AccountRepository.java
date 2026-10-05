package com.erp.accounting.repository;

import com.erp.accounting.entity.Account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountCode(String accountCode);

    boolean existsByAccountCode(String accountCode);

    List<Account> findByAccountType(String accountType);

    List<Account> findByStatus(String status);
}