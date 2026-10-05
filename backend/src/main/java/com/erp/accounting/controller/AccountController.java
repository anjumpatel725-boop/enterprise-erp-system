package com.erp.accounting.controller;

import com.erp.accounting.dto.AccountRequest;
import com.erp.accounting.dto.AccountResponse;
import com.erp.accounting.service.AccountService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounting/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody AccountRequest request) {

        AccountResponse account =
                accountService.createAccount(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(account);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {

        return ResponseEntity.ok(
                accountService.getAllAccounts()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                accountService.getAccountById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable Long id,
            @Valid @RequestBody AccountRequest request) {

        return ResponseEntity.ok(
                accountService.updateAccount(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(
            @PathVariable Long id) {

        accountService.deleteAccount(id);

        return ResponseEntity.ok(
                "Account deleted successfully"
        );
    }

    @GetMapping("/type/{accountType}")
    public ResponseEntity<List<AccountResponse>> getAccountsByType(
            @PathVariable String accountType) {

        return ResponseEntity.ok(
                accountService.getAccountsByType(accountType)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AccountResponse>> getAccountsByStatus(
            @PathVariable String status) {

        return ResponseEntity.ok(
                accountService.getAccountsByStatus(status)
        );
    }
}