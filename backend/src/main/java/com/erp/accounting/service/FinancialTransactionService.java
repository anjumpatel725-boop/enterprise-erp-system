package com.erp.accounting.service;

import com.erp.accounting.dto.TransactionRequest;
import com.erp.accounting.dto.TransactionResponse;
import com.erp.accounting.entity.Account;
import com.erp.accounting.entity.FinancialTransaction;
import com.erp.accounting.repository.AccountRepository;
import com.erp.accounting.repository.FinancialTransactionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FinancialTransactionService {

    private final FinancialTransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public FinancialTransactionService(
            FinancialTransactionRepository transactionRepository,
            AccountRepository accountRepository) {

        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public TransactionResponse createTransaction(TransactionRequest request) {

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        String transactionType =
                request.getTransactionType().trim().toUpperCase();

        if (!transactionType.equals("CREDIT")
                && !transactionType.equals("DEBIT")) {

            throw new RuntimeException(
                    "Transaction type must be CREDIT or DEBIT");
        }

        if (!account.getStatus().equalsIgnoreCase("ACTIVE")) {
            throw new RuntimeException(
                    "Cannot create transaction for inactive account");
        }

        FinancialTransaction transaction =
                new FinancialTransaction();

        transaction.setAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(transactionType);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setDescription(request.getDescription());

        FinancialTransaction savedTransaction =
                transactionRepository.save(transaction);

        return toResponse(savedTransaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getAllTransactions() {

        return transactionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(Long id) {

        FinancialTransaction transaction =
                transactionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Transaction not found"));

        return toResponse(transaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByAccount(
            Long accountId) {

        if (!accountRepository.existsById(accountId)) {
            throw new RuntimeException("Account not found");
        }

        return transactionRepository
                .findByAccountId(accountId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByType(
            String transactionType) {

        String type = transactionType.trim().toUpperCase();

        if (!type.equals("CREDIT") && !type.equals("DEBIT")) {
            throw new RuntimeException(
                    "Transaction type must be CREDIT or DEBIT");
        }

        return transactionRepository
                .findByTransactionType(type)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void deleteTransaction(Long id) {

        FinancialTransaction transaction =
                transactionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Transaction not found"));

        transactionRepository.delete(transaction);
    }

    private TransactionResponse toResponse(
            FinancialTransaction transaction) {

        Account account = transaction.getAccount();

        return new TransactionResponse(
                transaction.getId(),
                account.getId(),
                account.getAccountCode(),
                account.getName(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getTransactionDate(),
                transaction.getDescription()
        );
    }
}