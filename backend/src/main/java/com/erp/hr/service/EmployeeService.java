package com.erp.hr.service;

import com.erp.hr.dto.EmployeeRequest;
import com.erp.hr.dto.EmployeeResponse;
import com.erp.hr.entity.Department;
import com.erp.hr.entity.Employee;
import com.erp.hr.repository.DepartmentRepository;
import com.erp.hr.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository) {

        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    // =========================
    // CREATE EMPLOYEE
    // =========================

    public EmployeeResponse createEmployee(
            EmployeeRequest request) {

        if (employeeRepository
                .existsByEmployeeCode(request.getEmployeeCode())) {

            throw new RuntimeException(
                    "Employee code already exists"
            );
        }

        if (employeeRepository
                .existsByEmail(request.getEmail())) {

            throw new RuntimeException(
                    "Employee email already exists"
            );
        }

        Department department =
                departmentRepository
                        .findById(request.getDepartmentId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Department not found"
                                )
                        );

        Employee employee = new Employee();

        employee.setEmployeeCode(
                request.getEmployeeCode()
        );

        employee.setFirstName(
                request.getFirstName()
        );

        employee.setLastName(
                request.getLastName()
        );

        employee.setEmail(
                request.getEmail()
        );

        employee.setPhone(
                request.getPhone()
        );

        employee.setDateOfJoining(
                request.getDateOfJoining()
        );

        employee.setDesignation(
                request.getDesignation()
        );

        employee.setSalary(
                request.getSalary()
        );

        employee.setStatus(
                request.getStatus() == null ||
                request.getStatus().isBlank()
                        ? "ACTIVE"
                        : request.getStatus()
        );

        employee.setDepartment(department);

        Employee savedEmployee =
                employeeRepository.save(employee);

        return toResponse(savedEmployee);
    }


    // =========================
    // GET ALL EMPLOYEES
    // =========================

    public List<EmployeeResponse> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // =========================
    // GET EMPLOYEE BY ID
    // =========================

    public EmployeeResponse getEmployeeById(
            Long id) {

        Employee employee =
                employeeRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found"
                                )
                        );

        return toResponse(employee);
    }


    // =========================
    // UPDATE EMPLOYEE
    // =========================

    public EmployeeResponse updateEmployee(
            Long id,
            EmployeeRequest request) {

        Employee employee =
                employeeRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found"
                                )
                        );


        // Check employee code

        if (!employee.getEmployeeCode()
                .equals(request.getEmployeeCode())
                && employeeRepository
                    .existsByEmployeeCode(
                            request.getEmployeeCode())) {

            throw new RuntimeException(
                    "Employee code already exists"
            );
        }


        // Check email

        if (!employee.getEmail()
                .equals(request.getEmail())
                && employeeRepository
                    .existsByEmail(
                            request.getEmail())) {

            throw new RuntimeException(
                    "Employee email already exists"
            );
        }


        // Find department

        Department department =
                departmentRepository
                        .findById(request.getDepartmentId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Department not found"
                                )
                        );


        // Update fields

        employee.setEmployeeCode(
                request.getEmployeeCode()
        );

        employee.setFirstName(
                request.getFirstName()
        );

        employee.setLastName(
                request.getLastName()
        );

        employee.setEmail(
                request.getEmail()
        );

        employee.setPhone(
                request.getPhone()
        );

        employee.setDateOfJoining(
                request.getDateOfJoining()
        );

        employee.setDesignation(
                request.getDesignation()
        );

        employee.setSalary(
                request.getSalary()
        );

        employee.setStatus(
                request.getStatus() == null ||
                request.getStatus().isBlank()
                        ? "ACTIVE"
                        : request.getStatus()
        );

        employee.setDepartment(department);


        Employee updatedEmployee =
                employeeRepository.save(employee);

        return toResponse(updatedEmployee);
    }


    // =========================
    // DELETE EMPLOYEE
    // =========================

    public void deleteEmployee(Long id) {

        Employee employee =
                employeeRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found"
                                )
                        );

        employeeRepository.delete(employee);
    }


    // =========================
    // ENTITY -> RESPONSE DTO
    // =========================

    private EmployeeResponse toResponse(
            Employee employee) {

        Department department =
                employee.getDepartment();

        return new EmployeeResponse(
                employee.getId(),
                employee.getEmployeeCode(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getDateOfJoining(),
                employee.getDesignation(),
                employee.getSalary(),
                employee.getStatus(),
                department.getId(),
                department.getName()
        );
    }
}