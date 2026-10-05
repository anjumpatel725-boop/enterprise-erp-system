package com.erp.accounting.dto;

import java.math.BigDecimal;

public class AccountingReportResponse {

    private BigDecimal totalCredit;
    private BigDecimal totalDebit;
    private BigDecimal netBalance;

    public AccountingReportResponse() {
    }

    public AccountingReportResponse(
            BigDecimal totalCredit,
            BigDecimal totalDebit,
            BigDecimal netBalance) {

        this.totalCredit = totalCredit;
        this.totalDebit = totalDebit;
        this.netBalance = netBalance;
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

    public BigDecimal getNetBalance() {
        return netBalance;
    }

    public void setNetBalance(BigDecimal netBalance) {
        this.netBalance = netBalance;
    }
}