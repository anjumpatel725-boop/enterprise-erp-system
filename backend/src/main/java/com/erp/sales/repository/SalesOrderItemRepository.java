package com.erp.sales.repository;

import com.erp.sales.entity.SalesOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalesOrderItemRepository
        extends JpaRepository<SalesOrderItem, Long> {

    List<SalesOrderItem> findBySalesOrderId(Long salesOrderId);

    void deleteBySalesOrderId(Long salesOrderId);

    // =========================================================
    // BULK FETCH ITEMS FOR MULTIPLE ORDERS
    // =========================================================

    @Query("""
        SELECT i
        FROM SalesOrderItem i
        WHERE i.salesOrder.id IN :orderIds
    """)
    List<SalesOrderItem> findBySalesOrderIds(
            @Param("orderIds") List<Long> orderIds
    );
}