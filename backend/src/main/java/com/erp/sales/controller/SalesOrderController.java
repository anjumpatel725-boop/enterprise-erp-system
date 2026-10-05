package com.erp.sales.controller;

import com.erp.sales.dto.SalesOrderRequest;
import com.erp.sales.dto.SalesOrderResponse;
import com.erp.sales.dto.SalesOrderStatusRequest;
import com.erp.sales.service.SalesOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import java.util.List;

@RestController
@RequestMapping("/api/sales/orders")
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    public SalesOrderController(
            SalesOrderService salesOrderService) {

        this.salesOrderService =
                salesOrderService;
    }

    @PostMapping
    public ResponseEntity<SalesOrderResponse> createOrder(
            @RequestBody SalesOrderRequest request,
            Authentication authentication) {

        String username =
                authentication.getName();

        SalesOrderResponse response =
                salesOrderService.createOrder(
                        request,
                        username
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<SalesOrderResponse>>
    getAllOrders() {

        return ResponseEntity.ok(
                salesOrderService.getAllOrders()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesOrderResponse>
    getOrderById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                salesOrderService.getOrderById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesOrderResponse>
    updateOrder(
            @PathVariable Long id,
            @RequestBody SalesOrderRequest request) {

        return ResponseEntity.ok(
                salesOrderService.updateOrder(
                        id,
                        request
                )
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SalesOrderResponse>
    updateOrderStatus(
            @PathVariable Long id,
            @RequestBody SalesOrderStatusRequest request) {

        return ResponseEntity.ok(
                salesOrderService.updateOrderStatus(
                        id,
                        request.getStatus()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteOrder(
            @PathVariable Long id) {

        salesOrderService.deleteOrder(id);

        return ResponseEntity
                .noContent()
                .build();
    }
    @GetMapping("/paged")
public ResponseEntity<Page<SalesOrderResponse>>
getOrdersPaginated(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

    return ResponseEntity.ok(
            salesOrderService.getOrdersPaginated(
                    page,
                    size
            )
    );
}
}