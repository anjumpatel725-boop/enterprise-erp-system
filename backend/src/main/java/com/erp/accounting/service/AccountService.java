package com.erp.accounting.service;

import com.erp.accounting.dto.AccountRequest;
import com.erp.accounting.dto.AccountResponse;
import com.erp.accounting.entity.Account;
import com.erp.accounting.repository.AccountRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public AccountResponse createAccount(AccountRequest request) {

        String accountCode = request.getAccountCode().trim();

        if (accountRepository.existsByAccountCode(accountCode)) {
            throw new RuntimeException("Account code already exists");
        }

        Account account = new Account();

        account.setAccountCode(accountCode);
        account.setName(request.getName().trim());
        account.setDescription(request.getDescription());
        account.setAccountType(
                request.getAccountType().toUpperCase()
        );
        account.setStatus(
                request.getStatus().toUpperCase()
        );

        Account savedAccount = accountRepository.save(account);

        return toResponse(savedAccount);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAllAccounts() {

        return accountRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccountById(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Account not found")
                );

        return toResponse(account);
    }

    @Transactional
    public AccountResponse updateAccount(
            Long id,
            AccountRequest request) {

        Account account = accountRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Account not found")
                );

        String accountCode = request.getAccountCode().trim();

        if (!account.getAccountCode().equals(accountCode)
                && accountRepository.existsByAccountCode(accountCode)) {

            throw new RuntimeException(
                    "Account code already exists"
            );
        }

        account.setAccountCode(accountCode);
        account.setName(request.getName().trim());
        account.setDescription(request.getDescription());
        account.setAccountType(
                request.getAccountType().toUpperCase()
        );
        account.setStatus(
                request.getStatus().toUpperCase()
        );

        Account updatedAccount =
                accountRepository.save(account);

        return toResponse(updatedAccount);
    }

    @Transactional
    public void deleteAccount(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Account not found")
                );

        accountRepository.delete(account);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAccountsByType(
            String accountType) {

        return accountRepository
                .findByAccountType(accountType.toUpperCase())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAccountsByStatus(
            String status) {

        return accountRepository
                .findByStatus(status.toUpperCase())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private AccountResponse toResponse(Account account) {

        return new AccountResponse(
                account.getId(),
                account.getAccountCode(),
                account.getName(),
                account.getDescription(),
                account.getAccountType(),
                account.getStatus()
        );
    }
}