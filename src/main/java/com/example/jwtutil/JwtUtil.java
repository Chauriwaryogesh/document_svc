package com.example.jwtutil;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	 // Use a secret that is at least 32 characters (256 bits)
    private static final String SECRET = "your_very_long_secret_key_32_chars!";
    
    // Convert the secret to a proper key
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

// Generate token
public String generateToken(String username) {
    return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiry
            .signWith(SignatureAlgorithm.HS256, key)
            .compact();
}

// Validate token
public boolean validateToken(String token, String username) {
    String extractedUsername = extractUsername(token);
    return (extractedUsername.equals(username) && !isTokenExpired(token));
}

// Extract username
public String extractUsername(String token) {
    return extractClaims(token).getSubject();
}

// Extract expiration date
public Date extractExpiration(String token) {
    return extractClaims(token).getExpiration();
}

private Claims extractClaims(String token) {
    return Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(token)
            .getBody();
}

private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
}
}