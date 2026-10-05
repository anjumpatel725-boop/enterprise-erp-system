package com.erp.accounting.controller;

import com.erp.accounting.dto.AccountReportResponse;
import com.erp.accounting.dto.AccountingReportResponse;
import com.erp.accounting.service.AccountingReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounting/reports")
public class AccountingReportController {

    private final AccountingReportService reportService;

    public AccountingReportController(
            AccountingReportService reportService) {

        this.reportService = reportService;
    }

    // Overall accounting report
    @GetMapping("/summary")
    public ResponseEntity<AccountingReportResponse> getSummary() {

        AccountingReportResponse report =
                reportService.getOverallReport();

        return ResponseEntity.ok(report);
    }

    // Account-wise accounting report
    @GetMapping("/account/{accountId}")
    public ResponseEntity<AccountReportResponse> getAccountReport(
            @PathVariable Long accountId) {

        AccountReportResponse report =
                reportService.getAccountReport(accountId);

        return ResponseEntity.ok(report);
    }
}