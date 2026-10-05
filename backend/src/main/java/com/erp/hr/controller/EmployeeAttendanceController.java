package com.erp.hr.controller;

import com.erp.hr.dto.AttendanceResponse;
import com.erp.hr.entity.Employee;
import com.erp.hr.service.AttendanceService;
import com.erp.hr.service.EmployeeSelfService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee/attendance")
public class EmployeeAttendanceController {

    private final AttendanceService attendanceService;
    private final EmployeeSelfService employeeSelfService;

    public EmployeeAttendanceController(
            AttendanceService attendanceService,
            EmployeeSelfService employeeSelfService) {

        this.attendanceService = attendanceService;
        this.employeeSelfService = employeeSelfService;
    }

    // =========================
    // GET MY ATTENDANCE
    // =========================

    @GetMapping
    public ResponseEntity<List<AttendanceResponse>>
    getMyAttendance(
            Authentication authentication) {

        Employee employee =
                employeeSelfService
                        .getLoggedInEmployee(authentication);

        return ResponseEntity.ok(
                attendanceService
                        .getAttendanceByEmployee(
                                employee.getId()
                        )
        );
    }
}