package com.erp.hr.controller;

import com.erp.hr.dto.EmployeeProfileResponse;
import com.erp.hr.service.EmployeeProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee")
public class EmployeeProfileController {

    private final EmployeeProfileService employeeProfileService;

    public EmployeeProfileController(
            EmployeeProfileService employeeProfileService) {

        this.employeeProfileService = employeeProfileService;
    }

    @GetMapping("/profile")
    public EmployeeProfileResponse getMyProfile(
            Authentication authentication) {

        return employeeProfileService.getProfile(
                authentication.getName()
        );
    }
}