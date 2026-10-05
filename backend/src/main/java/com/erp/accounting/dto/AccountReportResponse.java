package com.erp.accounting.dto;

import java.math.BigDecimal;

public class AccountReportResponse {

    private Long accountId;
    private BigDecimal totalCredit;
    private BigDecimal totalDebit;
    private BigDecimal balance;

    public AccountReportResponse() {
    }

    public AccountReportResponse(
            Long accountId,
            BigDecimal totalCredit,
            BigDecimal totalDebit,
            BigDecimal balance) {

        this.accountId = accountId;
        this.totalCredit = totalCredit;
        this.totalDebit = totalDebit;
        this.balance = balance;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }

    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}