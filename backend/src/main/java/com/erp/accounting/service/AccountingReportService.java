package com.erp.accounting.service;

import com.erp.accounting.dto.AccountReportResponse;
import com.erp.accounting.dto.AccountingReportResponse;
import com.erp.accounting.entity.FinancialTransaction;
import com.erp.accounting.repository.FinancialTransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountingReportService {

    private final FinancialTransactionRepository transactionRepository;

    public AccountingReportService(
            FinancialTransactionRepository transactionRepository) {

        this.transactionRepository = transactionRepository;
    }

    // Account-wise report
    public AccountReportResponse getAccountReport(Long accountId) {

        List<FinancialTransaction> transactions =
                transactionRepository.findByAccountId(accountId);

        BigDecimal totalCredit = BigDecimal.ZERO;
        BigDecimal totalDebit = BigDecimal.ZERO;

        for (FinancialTransaction transaction : transactions) {

            if ("CREDIT".equalsIgnoreCase(
                    transaction.getTransactionType())) {

                totalCredit = totalCredit.add(
                        transaction.getAmount());

            } else if ("DEBIT".equalsIgnoreCase(
                    transaction.getTransactionType())) {

                totalDebit = totalDebit.add(
                        transaction.getAmount());
            }
        }

        BigDecimal balance =
                totalCredit.subtract(totalDebit);

        return new AccountReportResponse(
                accountId,
                totalCredit,
                totalDebit,
                balance
        );
    }

    // Overall accounting report
    public AccountingReportResponse getOverallReport() {

        List<FinancialTransaction> transactions =
                transactionRepository.findAll();

        BigDecimal totalCredit = BigDecimal.ZERO;
        BigDecimal totalDebit = BigDecimal.ZERO;

        for (FinancialTransaction transaction : transactions) {

            if ("CREDIT".equalsIgnoreCase(
                    transaction.getTransactionType())) {

                totalCredit = totalCredit.add(
                        transaction.getAmount());

            } else if ("DEBIT".equalsIgnoreCase(
                    transaction.getTransactionType())) {

                totalDebit = totalDebit.add(
                        transaction.getAmount());
            }
        }

        BigDecimal netBalance =
                totalCredit.subtract(totalDebit);

        return new AccountingReportResponse(
                totalCredit,
                totalDebit,
                netBalance
        );
    }
}