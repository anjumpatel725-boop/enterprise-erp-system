package com.erp.reporting.dto;

import java.math.BigDecimal;

public class DashboardReportResponse {

    private long totalEmployees;
    private long totalProducts;
    private long totalCustomers;
    private long totalOrders;

    private BigDecimal totalSales;

    private long pendingOrders;
    private long lowStockProducts;

    public DashboardReportResponse() {
    }

    public DashboardReportResponse(
            long totalEmployees,
            long totalProducts,
            long totalCustomers,
            long totalOrders,
            BigDecimal totalSales,
            long pendingOrders,
            long lowStockProducts) {

        this.totalEmployees = totalEmployees;
        this.totalProducts = totalProducts;
        this.totalCustomers = totalCustomers;
        this.totalOrders = totalOrders;
        this.totalSales = totalSales;
        this.pendingOrders = pendingOrders;
        this.lowStockProducts = lowStockProducts;
    }

    public long getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    public long getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(long pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public long getLowStockProducts() {
        return lowStockProducts;
    }

    public void setLowStockProducts(long lowStockProducts) {
        this.lowStockProducts = lowStockProducts;
    }
}