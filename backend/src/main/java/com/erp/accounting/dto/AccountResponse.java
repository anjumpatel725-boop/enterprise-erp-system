package com.erp.accounting.dto;

public class AccountResponse {

    private Long id;
    private String accountCode;
    private String name;
    private String description;
    private String accountType;
    private String status;

    public AccountResponse() {
    }

    public AccountResponse(
            Long id,
            String accountCode,
            String name,
            String description,
            String accountType,
            String status) {

        this.id = id;
        this.accountCode = accountCode;
        this.name = name;
        this.description = description;
        this.accountType = accountType;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getStatus() {
        return status;
    }
}