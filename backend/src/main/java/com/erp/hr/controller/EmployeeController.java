package com.erp.hr.controller;

import com.erp.hr.dto.EmployeeRequest;
import com.erp.hr.dto.EmployeeResponse;
import com.erp.hr.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hr/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(
            EmployeeService employeeService) {

        this.employeeService = employeeService;
    }


    // =========================
    // CREATE EMPLOYEE
    // =========================

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeRequest request) {

        EmployeeResponse employee =
                employeeService.createEmployee(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(employee);
    }


    // =========================
    // GET ALL EMPLOYEES
    // =========================

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>>
    getAllEmployees() {

        return ResponseEntity.ok(
                employeeService.getAllEmployees()
        );
    }


    // =========================
    // GET EMPLOYEE BY ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse>
    getEmployeeById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                employeeService.getEmployeeById(id)
        );
    }


    // =========================
    // UPDATE EMPLOYEE
    // =========================

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse>
    updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(
                        id,
                        request
                )
        );
    }


    // =========================
    // DELETE EMPLOYEE
    // =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(
            @PathVariable Long id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.ok(
                "Employee deleted successfully"
        );
    }
}