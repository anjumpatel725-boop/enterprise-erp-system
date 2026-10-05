package com.erp.auth.security;

import com.erp.auth.service.CustomUserDetailsService;
import com.erp.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // =========================
        // CORS PREFLIGHT
        // =========================
        //
        // Browser sends OPTIONS request
        // before actual POST/GET request.
        //
        // OPTIONS request does not need JWT.
        // Let Spring Security CORS handling
        // process it normally.
        // =========================

        if ("OPTIONS".equalsIgnoreCase(
                request.getMethod())) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        // =========================
        // GET AUTHORIZATION HEADER
        // =========================

        String authHeader =
                request.getHeader("Authorization");

        // =========================
        // NO TOKEN
        // =========================

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        // =========================
        // EXTRACT TOKEN
        // =========================

        String token =
                authHeader.substring(7);

        try {

            // =========================
            // VALIDATE TOKEN
            // =========================

            if (!jwtService.isTokenValid(token)) {

                System.out.println(
                        "JWT TOKEN INVALID"
                );

                filterChain.doFilter(
                        request,
                        response
                );

                return;
            }

            // =========================
            // EXTRACT USERNAME
            // =========================

            String username =
                    jwtService.extractUsername(token);

            // =========================
            // LOAD USER
            // =========================

            var userDetails =
                    userDetailsService
                            .loadUserByUsername(username);

            // =========================
            // GET AUTHORITIES
            // =========================
            //
            // Authorities come from
            // CustomUserDetailsService.
            //
            // This avoids role mismatch
            // between JWT and database.
            // =========================

            var authorities =
                    userDetails.getAuthorities();

            System.out.println(
                    "JWT USERNAME = " +
                    username
            );

            System.out.println(
                    "JWT AUTHORITIES = " +
                    authorities
            );

            // =========================
            // SET AUTHENTICATION
            // =========================

            if (SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                authorities
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(
                                authentication
                        );
            }

        } catch (Exception e) {

            // =========================
            // JWT ERROR
            // =========================

            System.out.println(
                    "JWT ERROR = " +
                    e.getMessage()
            );

            e.printStackTrace();
        }

        // =========================
        // CONTINUE FILTER CHAIN
        // =========================

        filterChain.doFilter(
                request,
                response
        );
    }
}
