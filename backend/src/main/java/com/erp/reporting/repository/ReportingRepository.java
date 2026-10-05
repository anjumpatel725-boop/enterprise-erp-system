package com.erp.reporting.repository;

import com.erp.sales.entity.SalesOrderStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class ReportingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public long getTotalEmployees() {

        Long count = entityManager.createQuery(
                "SELECT COUNT(e) FROM Employee e",
                Long.class
        ).getSingleResult();

        return count != null ? count : 0;
    }

    public long getTotalProducts() {

        Long count = entityManager.createQuery(
                "SELECT COUNT(p) FROM Product p",
                Long.class
        ).getSingleResult();

        return count != null ? count : 0;
    }

    public long getTotalCustomers() {

        Long count = entityManager.createQuery(
                "SELECT COUNT(c) FROM Customer c",
                Long.class
        ).getSingleResult();

        return count != null ? count : 0;
    }

    public long getTotalOrders() {

        Long count = entityManager.createQuery(
                "SELECT COUNT(o) FROM SalesOrder o",
                Long.class
        ).getSingleResult();

        return count != null ? count : 0;
    }

    public java.math.BigDecimal getTotalSales() {

        java.math.BigDecimal total = entityManager.createQuery(
                """
                SELECT COALESCE(SUM(o.totalAmount), 0)
                FROM SalesOrder o
                WHERE o.status <> :cancelled
                """,
                java.math.BigDecimal.class
        ).setParameter("cancelled", SalesOrderStatus.CANCELLED)
         .getSingleResult();

        return total != null ? total : java.math.BigDecimal.ZERO;
    }

    public long getPendingOrders() {

        Long count = entityManager.createQuery(
                """
                SELECT COUNT(o)
                FROM SalesOrder o
                WHERE o.status = :status
                """,
                Long.class
        ).setParameter("status", SalesOrderStatus.PENDING)
         .getSingleResult();

        return count != null ? count : 0;
    }

    public long getLowStockProducts() {

        Long count = entityManager.createQuery(
                """
                SELECT COUNT(p)
                FROM Product p
                WHERE p.quantity <= p.reorderLevel
                """,
                Long.class
        ).getSingleResult();

        return count != null ? count : 0;
    }
}