package com.erp.hr.service;

import com.erp.auth.entity.User;
import com.erp.auth.repository.UserRepository;
import com.erp.hr.entity.Employee;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class EmployeeSelfService {

    private final UserRepository userRepository;

    public EmployeeSelfService(
            UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public Employee getLoggedInEmployee(
            Authentication authentication) {

        if (authentication == null ||
                authentication.getName() == null) {

            throw new RuntimeException(
                    "User is not authenticated"
            );
        }

        String username =
                authentication.getName();

        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        if (user.getEmployee() == null) {

            throw new RuntimeException(
                    "No employee profile linked to this user"
            );
        }

        return user.getEmployee();
    }
}