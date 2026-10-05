package com.erp.accounting.repository;

import com.erp.accounting.entity.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialTransactionRepository
        extends JpaRepository<FinancialTransaction, Long> {

    List<FinancialTransaction> findByAccountId(Long accountId);

    List<FinancialTransaction> findByTransactionType(String transactionType);
}