package com.erp.reporting.controller;

import com.erp.reporting.dto.DashboardReportResponse;
import com.erp.reporting.service.ReportingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardReportResponse> getDashboardReport() {

        return ResponseEntity.ok(
                reportingService.getDashboardReport()
        );
    }
}