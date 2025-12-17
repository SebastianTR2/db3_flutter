package com.app.salesmasterpro.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretValue;

    @Value("${jwt.expiration}")
    private Long expirationTime;

    private Key key;

    @PostConstruct
    public void initKey() {
        if (secretValue == null || secretValue.isBlank()) {
            throw new IllegalStateException("JWT_SECRET is not configured");
        }

        String secret = secretValue.trim();

        try {
            byte[] decodedKey = Decoders.BASE64.decode(secret);
            if (decodedKey.length >= 32) {
                this.key = Keys.hmacShaKeyFor(decodedKey);
                System.out.println("🔐 JWT_SECRET loaded as Base64");
                return;
            }
        } catch (Exception ignored) {
        }

        if (secret.length() < 64) {
            System.out.println("⚠ Warning: JWT_SECRET should be at least 64 characters for HS512");
        }

        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        System.out.println("🔐 JWT_SECRET loaded as plain text");
    }

    private Key getSignInKey() {
        if (key == null) {
            initKey();
        }
        return key;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(getSignInKey(), SignatureAlgorithm.HS512)
            .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
