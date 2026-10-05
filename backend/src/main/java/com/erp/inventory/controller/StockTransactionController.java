package com.erp.inventory.controller;

import com.erp.inventory.dto.StockTransactionRequest;
import com.erp.inventory.dto.StockTransactionResponse;
import com.erp.inventory.service.StockTransactionService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/stock")
public class StockTransactionController {

    private final StockTransactionService stockTransactionService;

    public StockTransactionController(
            StockTransactionService stockTransactionService) {

        this.stockTransactionService = stockTransactionService;
    }

    @PostMapping
    public ResponseEntity<StockTransactionResponse> createTransaction(
            @Valid @RequestBody StockTransactionRequest request) {

        StockTransactionResponse transaction =
                stockTransactionService.createTransaction(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transaction);
    }

    @GetMapping
    public ResponseEntity<List<StockTransactionResponse>>
    getAllTransactions() {

        return ResponseEntity.ok(
                stockTransactionService.getAllTransactions()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockTransactionResponse>
    getTransactionById(@PathVariable Long id) {

        return ResponseEntity.ok(
                stockTransactionService.getTransactionById(id)
        );
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<StockTransactionResponse>>
    getTransactionsByProduct(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                stockTransactionService
                        .getTransactionsByProduct(productId)
        );
    }

    @GetMapping("/type/{transactionType}")
    public ResponseEntity<List<StockTransactionResponse>>
    getTransactionsByType(
            @PathVariable String transactionType) {

        return ResponseEntity.ok(
                stockTransactionService
                        .getTransactionsByType(transactionType)
        );
    }
}