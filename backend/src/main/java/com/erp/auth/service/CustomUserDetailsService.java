package com.erp.auth.service;

import com.erp.auth.entity.User;
import com.erp.auth.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(
            UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(
            String username)
            throws UsernameNotFoundException {

        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "User not found: " +
                                        username
                                )
                        );

        // =========================
        // USER ROLE
        // =========================

        String role =
                user.getRole().name();

        System.out.println(
                "DATABASE USER = " +
                user.getUsername()
        );

        System.out.println(
                "DATABASE ROLE = " +
                role
        );

        // =========================
        // AUTHORITY
        // =========================

        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(
                        "ROLE_" + role
                );

        System.out.println(
                "DATABASE AUTHORITY = " +
                authority.getAuthority()
        );

        // =========================
        // USER DETAILS
        // =========================

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isActive(),
                true,
                true,
                true,
                List.of(authority)
        );
    }
}