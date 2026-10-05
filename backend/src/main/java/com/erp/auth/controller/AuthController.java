package com.erp.auth.controller;

import com.erp.auth.dto.EmployeeLoginRequest;
import com.erp.auth.dto.LoginRequest;
import com.erp.auth.dto.RegisterRequest;
import com.erp.auth.entity.User;
import com.erp.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    // =========================
    // NORMAL REGISTER
    // =========================

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request) {

        User user =
                authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        Map.of(
                                "message",
                                "User registered successfully",

                                "username",
                                user.getUsername(),

                                "email",
                                user.getEmail(),

                                "role",
                                user.getRole()
                        )
                );
    }


    // =========================
    // CREATE EMPLOYEE LOGIN
    // =========================

    @PostMapping(
            "/employee/{employeeId}/login"
    )
    public ResponseEntity<?> createEmployeeLogin(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeLoginRequest request) {

        User user =
                authService.createEmployeeLogin(
                        employeeId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        Map.of(
                                "message",
                                "Employee login created successfully",

                                "username",
                                user.getUsername(),

                                "email",
                                user.getEmail(),

                                "role",
                                user.getRole(),

                                "employeeId",
                                employeeId
                        )
                );
    }


    // =========================
    // LOGIN
    // =========================

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request) {

        // Authenticate user and generate JWT
        String token =
                authService.login(request);

        // Get logged-in user
        User user =
                authService.getUserByUsername(
                        request.getUsername()
                );

        // Return complete login information
        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Login successful",

                        "token",
                        token,

                        "username",
                        user.getUsername(),

                        "email",
                        user.getEmail(),

                        "role",
                        user.getRole()
                )
        );
    }
}