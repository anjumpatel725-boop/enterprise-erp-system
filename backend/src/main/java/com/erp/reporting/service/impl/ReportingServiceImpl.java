package com.erp.reporting.service.impl;

import com.erp.reporting.dto.DashboardReportResponse;
import com.erp.reporting.repository.ReportingRepository;
import com.erp.reporting.service.ReportingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportingServiceImpl implements ReportingService {

    private final ReportingRepository reportingRepository;

    public ReportingServiceImpl(ReportingRepository reportingRepository) {
        this.reportingRepository = reportingRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardReportResponse getDashboardReport() {


        return new DashboardReportResponse(
                reportingRepository.getTotalEmployees(),
                reportingRepository.getTotalProducts(),
                reportingRepository.getTotalCustomers(),
                reportingRepository.getTotalOrders(),
                reportingRepository.getTotalSales(),
                reportingRepository.getPendingOrders(),
                reportingRepository.getLowStockProducts()
        );
    }
}