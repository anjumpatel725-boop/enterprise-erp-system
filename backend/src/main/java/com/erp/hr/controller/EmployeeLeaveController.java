package com.erp.hr.controller;

import com.erp.hr.dto.EmployeeLeaveRequest;
import com.erp.hr.dto.LeaveRequest;
import com.erp.hr.dto.LeaveResponse;
import com.erp.hr.entity.Employee;
import com.erp.hr.service.EmployeeSelfService;
import com.erp.hr.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee/leaves")
public class EmployeeLeaveController {

    private final LeaveService leaveService;
    private final EmployeeSelfService employeeSelfService;

    public EmployeeLeaveController(
            LeaveService leaveService,
            EmployeeSelfService employeeSelfService) {

        this.leaveService = leaveService;
        this.employeeSelfService = employeeSelfService;
    }

    // ==========================================
    // GET MY LEAVES
    // ==========================================

    @GetMapping
    public ResponseEntity<List<LeaveResponse>> getMyLeaves(
            Authentication authentication) {

        Employee employee =
                employeeSelfService
                        .getLoggedInEmployee(authentication);

        return ResponseEntity.ok(
                leaveService.getLeavesByEmployee(
                        employee.getId()
                )
        );
    }

    // ==========================================
    // APPLY LEAVE
    // ==========================================

    @PostMapping
    public ResponseEntity<LeaveResponse> applyLeave(
            @Valid @RequestBody EmployeeLeaveRequest request,
            Authentication authentication) {

        // Get logged-in employee from JWT
        Employee employee =
                employeeSelfService
                        .getLoggedInEmployee(authentication);

        // Create internal LeaveRequest
        LeaveRequest leaveRequest =
                new LeaveRequest();

        // Employee ID comes from logged-in user
        leaveRequest.setEmployeeId(
                employee.getId()
        );

        leaveRequest.setLeaveType(
                request.getLeaveType()
        );

        leaveRequest.setStartDate(
                request.getStartDate()
        );

        leaveRequest.setEndDate(
                request.getEndDate()
        );

        leaveRequest.setReason(
                request.getReason()
        );

        // Employee cannot choose status
        leaveRequest.setStatus(
                "PENDING"
        );

        LeaveResponse leave =
                leaveService.createLeave(
                        leaveRequest
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(leave);
    }
}