package com.erp.auth.service;

import com.erp.auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

private final String secretKey;

private static final long EXPIRATION_TIME =
        1000 * 60 * 60; // 1 hour

public JwtService(
        @Value("${jwt.secret}") String secretKey) {

    this.secretKey = secretKey;
}

private SecretKey getSigningKey() {

    return Keys.hmacShaKeyFor(
            secretKey.getBytes(StandardCharsets.UTF_8)
    );
}

public String generateToken(User user) {

    return Jwts.builder()
            .subject(user.getUsername())
            .claim("userId", user.getId())
            .claim("email", user.getEmail())
            .claim("role", user.getRole().name())
            .issuedAt(new Date())
            .expiration(
                    new Date(
                            System.currentTimeMillis()
                                    + EXPIRATION_TIME
                    )
            )
            .signWith(getSigningKey())
            .compact();
}

public String extractUsername(String token) {

    return getClaims(token).getSubject();
}

public String extractRole(String token) {

    return getClaims(token)
            .get("role", String.class);
}

public boolean isTokenValid(String token) {

    try {

        Claims claims = getClaims(token);

        return claims
                .getExpiration()
                .after(new Date());

    } catch (Exception e) {

        return false;
    }
}

private Claims getClaims(String token) {

    return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
}

}
