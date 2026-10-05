package com.erp.inventory.dto;

import java.time.LocalDateTime;

public class StockTransactionResponse {

    private Long id;

    private Long productId;
    private String productCode;
    private String productName;

    private Long supplierId;
    private String supplierName;

    private String transactionType;
    private Integer quantity;
    private LocalDateTime transactionDate;
    private String remarks;

    public StockTransactionResponse() {
    }

    public StockTransactionResponse(
            Long id,
            Long productId,
            String productCode,
            String productName,
            Long supplierId,
            String supplierName,
            String transactionType,
            Integer quantity,
            LocalDateTime transactionDate,
            String remarks) {

        this.id = id;
        this.productId = productId;
        this.productCode = productCode;
        this.productName = productName;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.transactionDate = transactionDate;
        this.remarks = remarks;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductName() {
        return productName;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public String getRemarks() {
        return remarks;
    }
}