package com.erp.inventory.dto;

import java.math.BigDecimal;

public class ProductResponse {

    private Long id;
    private String productCode;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private Integer reorderLevel;
    private String status;
    private Long categoryId;
    private String categoryName;

    public ProductResponse() {
    }

    public ProductResponse(
            Long id,
            String productCode,
            String name,
            String description,
            BigDecimal price,
            Integer quantity,
            Integer reorderLevel,
            String status,
            Long categoryId,
            String categoryName) {

        this.id = id;
        this.productCode = productCode;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
        this.status = status;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getReorderLevel() {
        return reorderLevel;
    }

    public String getStatus() {
        return status;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}