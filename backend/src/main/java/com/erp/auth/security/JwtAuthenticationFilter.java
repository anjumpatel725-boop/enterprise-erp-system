package com.erp.auth.security;

import com.erp.auth.service.CustomUserDetailsService;
import com.erp.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

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
            // Use authorities from
            // CustomUserDetailsService.
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

            System.out.println(
                    "JWT ERROR = " +
                    e.getMessage()
            );

            e.printStackTrace();
        }

        filterChain.doFilter(
                request,
                response
        );
    }
}