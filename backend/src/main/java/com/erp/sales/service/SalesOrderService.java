package com.erp.sales.service;

import com.erp.sales.dto.SalesOrderRequest;
import com.erp.sales.dto.SalesOrderResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SalesOrderService {

    SalesOrderResponse createOrder(
            SalesOrderRequest request,
            String username
    );

    List<SalesOrderResponse> getAllOrders();

    Page<SalesOrderResponse> getOrdersPaginated(
            int page,
            int size
    );

    SalesOrderResponse getOrderById(Long id);

    SalesOrderResponse updateOrder(
            Long id,
            SalesOrderRequest request
    );

    SalesOrderResponse updateOrderStatus(
            Long id,
            String newStatus
    );

    void deleteOrder(Long id);
}