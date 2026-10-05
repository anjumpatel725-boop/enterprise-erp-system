package com.erp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RbacTestController {

    @GetMapping("/api/hr/test")
    public String hrTest(Authentication authentication) {

        return "HR access granted to: "
                + authentication.getName();
    }

    @GetMapping("/api/inventory/test")
    public String inventoryTest(Authentication authentication) {

        return "Inventory access granted to: "
                + authentication.getName();
    }

    @GetMapping("/api/accounting/test")
    public String accountingTest(Authentication authentication) {

        return "Accounting access granted to: "
                + authentication.getName();
    }

    @GetMapping("/api/sales/test")
    public String salesTest(Authentication authentication) {

        return "Sales access granted to: "
                + authentication.getName();
    }

    @GetMapping("/api/reports/test")
    public String reportsTest(Authentication authentication) {

        return "Reports access granted to: "
                + authentication.getName();
    }
}