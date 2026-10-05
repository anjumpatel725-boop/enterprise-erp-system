package com.erp.accounting.controller;

import com.erp.accounting.dto.TransactionRequest;
import com.erp.accounting.dto.TransactionResponse;
import com.erp.accounting.service.FinancialTransactionService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounting/transactions")
public class FinancialTransactionController {

    private final FinancialTransactionService transactionService;

    public FinancialTransactionController(
            FinancialTransactionService transactionService) {

        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody TransactionRequest request) {

        TransactionResponse transaction =
                transactionService.createTransaction(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transaction);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>>
    getAllTransactions() {

        return ResponseEntity.ok(
                transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                transactionService.getTransactionById(id));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>>
    getTransactionsByAccount(
            @PathVariable Long accountId) {

        return ResponseEntity.ok(
                transactionService
                        .getTransactionsByAccount(accountId));
    }

    @GetMapping("/type/{transactionType}")
    public ResponseEntity<List<TransactionResponse>>
    getTransactionsByType(
            @PathVariable String transactionType) {

        return ResponseEntity.ok(
                transactionService
                        .getTransactionsByType(transactionType));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(
            @PathVariable Long id) {

        transactionService.deleteTransaction(id);

        return ResponseEntity.ok(
                "Transaction deleted successfully");
    }
}