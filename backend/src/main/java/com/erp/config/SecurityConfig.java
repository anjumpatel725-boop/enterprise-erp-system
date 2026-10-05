package com.erp.config;

import com.erp.auth.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    // =========================
    // PASSWORD ENCODER
    // =========================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    // =========================
    // SECURITY FILTER CHAIN
    // =========================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

            // =========================
            // CSRF
            // =========================

            .csrf(csrf -> csrf.disable())


            // =========================
            // CORS
            // =========================

            .cors(cors ->
                    cors.configurationSource(
                            corsConfigurationSource()
                    )
            )


            // =========================
            // SESSION
            // JWT = STATELESS
            // =========================

            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )


            // =========================
            // SECURITY HEADERS
            // =========================

            .headers(headers -> headers

                    .frameOptions(frame ->
                            frame.deny()
                    )

                    .contentTypeOptions(
                            contentType -> {}
                    )

                    .httpStrictTransportSecurity(hsts ->
                            hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000)
                    )
            )


            // =========================
            // AUTHORIZATION
            // =========================

            .authorizeHttpRequests(auth -> auth


                // =========================
                // CORS PREFLIGHT
                // =========================

                .requestMatchers(
                        HttpMethod.OPTIONS,
                        "/**"
                ).permitAll()


                // =========================
                // PUBLIC ENDPOINTS
                // =========================

                .requestMatchers(
                        "/api/auth/login",
                        "/api/auth/register",
                        "/api/health",
                        "/actuator/health"
                ).permitAll()


                // =========================
                // EMPLOYEE LOGIN CREATION
                // =========================
                // Only ADMIN / HR_ADMIN
                // can create employee login
                // =========================

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/auth/employee/*/login"
                ).hasAnyRole(
                        "ADMIN",
                        "HR_ADMIN"
                )


                // =========================
                // EMPLOYEE SELF SERVICE
                // =========================
                //
                // Employee can access:
                // /api/employee/**
                //
                // Backend decides which employee
                // using JWT + users.employee_id
                // =========================

                .requestMatchers(
                        "/api/employee/**"
                ).hasAnyRole(
                        "EMPLOYEE",
                        "ADMIN",
                        "HR_ADMIN"
                )


                // =========================
                // HR
                // =========================
                //
                // Employee CANNOT access
                // generic HR endpoints
                // =========================

                .requestMatchers(
                        "/api/hr/**"
                ).hasAnyRole(
                        "ADMIN",
                        "HR_ADMIN"
                )


                // =========================
                // INVENTORY
                // =========================

                .requestMatchers(
                        "/api/inventory/**"
                ).hasAnyRole(
                        "ADMIN",
                        "INVENTORY_MANAGER",
                        "SALES_MANAGER"
                )


                // =========================
                // ACCOUNTING
                // =========================

                .requestMatchers(
                        "/api/accounting/**"
                ).hasAnyRole(
                        "ADMIN",
                        "ACCOUNTANT"
                )


                // =========================
                // SALES
                // =========================

                .requestMatchers(
                        "/api/sales/**"
                ).hasAnyRole(
                        "ADMIN",
                        "SALES_MANAGER",
                        "HR_ADMIN"
                )


                // =========================
                // REPORTS
                // =========================

                .requestMatchers(
                        "/api/reports/**"
                ).hasAnyRole(
                        "ADMIN",
                        "SALES_MANAGER",
                        "HR_ADMIN",
                        "ACCOUNTANT",
                        "INVENTORY_MANAGER"
                )


                // =========================
                // AUDIT LOGS
                // =========================

                .requestMatchers(
                        "/api/audit/**"
                ).hasAnyRole(
                        "ADMIN",
                        "HR_ADMIN",
                        "SALES_MANAGER"
                )


                // =========================
                // EVERYTHING ELSE
                // =========================

                .anyRequest().authenticated()
            )


            // =========================
            // JWT FILTER
            // =========================

            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            );


        return http.build();
    }


    // =========================
    // CORS CONFIGURATION
    // =========================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();


        // =========================
        // FRONTEND
        // =========================

        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:5173"
                )
        );


        // =========================
        // HTTP METHODS
        // =========================

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );


        // =========================
        // HEADERS
        // =========================

        configuration.setAllowedHeaders(
                List.of(
                        "Authorization",
                        "Content-Type",
                        "Accept"
                )
        );


        // =========================
        // CREDENTIALS
        // =========================

        configuration.setAllowCredentials(false);


        // =========================
        // REGISTER CORS
        // =========================

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );


        return source;
    }
}