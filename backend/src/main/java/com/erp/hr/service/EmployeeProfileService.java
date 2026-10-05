package com.erp.hr.service;

import com.erp.auth.entity.User;
import com.erp.auth.repository.UserRepository;
import com.erp.hr.dto.EmployeeProfileResponse;
import com.erp.hr.entity.Employee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeProfileService {

    private final UserRepository userRepository;

    public EmployeeProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public EmployeeProfileResponse getProfile(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        Employee employee = user.getEmployee();

        if (employee == null) {
            throw new RuntimeException(
                    "No employee profile linked to this user"
            );
        }

        EmployeeProfileResponse response =
                new EmployeeProfileResponse();

        response.setId(employee.getId());
        response.setEmployeeCode(employee.getEmployeeCode());
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setEmail(employee.getEmail());
        response.setPhone(employee.getPhone());
        response.setDateOfJoining(employee.getDateOfJoining());
        response.setDesignation(employee.getDesignation());
        response.setSalary(employee.getSalary());
        response.setStatus(employee.getStatus());

        if (employee.getDepartment() != null) {

            response.setDepartmentId(
                    employee.getDepartment().getId()
            );

            response.setDepartmentName(
                    employee.getDepartment().getName()
            );
        }

        return response;
    }
}