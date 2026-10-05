package com.erp.auth.service;

import com.erp.auth.dto.EmployeeLoginRequest;
import com.erp.auth.dto.LoginRequest;
import com.erp.auth.dto.RegisterRequest;
import com.erp.auth.entity.Role;
import com.erp.auth.entity.User;
import com.erp.auth.repository.UserRepository;
import com.erp.hr.entity.Employee;
import com.erp.hr.repository.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmployeeRepository employeeRepository;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            EmployeeRepository employeeRepository) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.employeeRepository = employeeRepository;
    }

    // =========================
    // NORMAL REGISTER
    // =========================

    public User register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException(
                    "Username already exists"
            );
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(
                    "Email already exists"
            );
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(request.getRole());
        user.setActive(true);

        return userRepository.save(user);
    }


    // =========================
    // CREATE EMPLOYEE LOGIN
    // =========================

    public User createEmployeeLogin(
            Long employeeId,
            EmployeeLoginRequest request) {

        // Check username
        if (userRepository.existsByUsername(
                request.getUsername())) {

            throw new RuntimeException(
                    "Username already exists"
            );
        }

        // Check email
        if (userRepository.existsByEmail(
                request.getEmail())) {

            throw new RuntimeException(
                    "Email already exists"
            );
        }

        // Find employee
        Employee employee =
                employeeRepository
                        .findById(employeeId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found"
                                )
                        );

        // Check whether employee already has login
        if (userRepository
                .findByEmployeeId(employeeId)
                .isPresent()) {

            throw new RuntimeException(
                    "This employee already has a login account"
            );
        }

        // Create user
        User user = new User();

        user.setUsername(
                request.getUsername()
        );

        user.setEmail(
                request.getEmail()
        );

        // BCrypt password
        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        // Employee role automatically
        user.setRole(Role.EMPLOYEE);

        user.setActive(true);

        // Link employee with login
        user.setEmployee(employee);

        return userRepository.save(user);
    }


    // =========================
    // LOGIN
    // =========================

    public String login(LoginRequest request) {

        User user = userRepository
                .findByUsername(
                        request.getUsername()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid username or password"
                        )
                );

        if (!user.isActive()) {
            throw new RuntimeException(
                    "User account is inactive"
            );
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {

            throw new RuntimeException(
                    "Invalid username or password"
            );
        }

        return jwtService.generateToken(user);
    }


    // =========================
    // GET USER BY USERNAME
    // =========================

    public User getUserByUsername(
            String username) {

        return userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );
    }
}