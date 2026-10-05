package com.erp.accounting.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {

    private Long id;
    private Long accountId;
    private String accountCode;
    private String accountName;
    private BigDecimal amount;
    private String transactionType;
    private LocalDateTime transactionDate;
    private String description;

    public TransactionResponse() {
    }

    public TransactionResponse(
            Long id,
            Long accountId,
            String accountCode,
            String accountName,
            BigDecimal amount,
            String transactionType,
            LocalDateTime transactionDate,
            String description) {

        this.id = id;
        this.accountId = accountId;
        this.accountCode = accountCode;
        this.accountName = accountName;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public String getDescription() {
        return description;
    }
}