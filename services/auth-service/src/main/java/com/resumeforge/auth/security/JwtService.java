package com.resumeforge.auth.security;

import com.resumeforge.auth.exception.InvalidJwtException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT (JSON Web Token) service for token generation and validation.
 *
 * Handles all JWT operations using HS256 symmetric encryption.
 * Provides access token and refresh token generation with token hashing.
 */
@Slf4j
@Service
public class JwtService {

    @Getter
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Getter
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Getter
    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    private SecretKey cachedSigningKey;

    @PostConstruct
    public void init() {
        this.cachedSigningKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Get the signing key for JWT operations
     */
    private SecretKey getSigningKey() {
        return cachedSigningKey;
    }

    /**
     * Generate a JWT access token with subject (user ID)
     *
     * @param subject the subject (user ID UUID)
     * @return JWT token string
     */
    public String generateAccessToken(String subject) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");
        return createToken(claims, subject, jwtExpiration);
    }

    /**
     * Generate a JWT access token with additional claims
     *
     * @param subject the subject (user ID UUID)
     * @param claims additional claims to include in token
     * @return JWT token string
     */
    public String generateAccessToken(String subject, Map<String, Object> claims) {
        Map<String, Object> allClaims = new HashMap<>(claims);
        allClaims.put("type", "access");
        return createToken(allClaims, subject, jwtExpiration);
    }

    /**
     * Generate a JWT refresh token
     *
     * @param subject the subject (user ID UUID)
     * @return refresh JWT token string
     */
    public String generateRefreshToken(String subject) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return createToken(claims, subject, refreshExpiration);
    }

    /**
     * Hash a token for storage in database
     *
     * @param token the token to hash
     * @return hashed token
     */
    public String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException ex) {
            log.error("Failed to hash token", ex);
            throw new RuntimeException("Token hashing failed", ex);
        }
    }

    /**
     * Validate a JWT token
     *
     * @param token the JWT token to validate
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.warn("JWT token is expired: {}", ex.getMessage());
            return false;
        } catch (UnsupportedJwtException ex) {
            log.warn("JWT token is unsupported: {}", ex.getMessage());
            return false;
        } catch (MalformedJwtException ex) {
            log.warn("Invalid JWT token: {}", ex.getMessage());
            return false;
        } catch (SignatureException ex) {
            log.warn("JWT signature validation failed: {}", ex.getMessage());
            return false;
        } catch (IllegalArgumentException ex) {
            log.warn("JWT claims string is empty: {}", ex.getMessage());
            return false;
        }
    }

    /**
     * Extract subject (user ID) from token
     *
     * @param token the JWT token
     * @return subject string
     * @throws InvalidJwtException if token is invalid or expired
     */
    public String extractSubject(String token) throws InvalidJwtException {
        try {
            return extractAllClaims(token).getSubject();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException |
                 SignatureException | IllegalArgumentException ex) {
            log.error("Failed to extract subject from token: {}", ex.getMessage());
            throw new InvalidJwtException("Unable to extract subject from token", ex);
        }
    }

    /**
     * Extract all claims from token
     *
     * @param token the JWT token
     * @return Claims object
     * @throws InvalidJwtException if token is invalid
     */
    public Claims extractAllClaims(String token) throws InvalidJwtException {
        try {
            return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException |
                 SignatureException | IllegalArgumentException ex) {
            log.error("Failed to extract claims: {}", ex.getMessage());
            throw new InvalidJwtException("Unable to extract claims from token", ex);
        }
    }

    /**
     * Check if token is expired
     *
     * @param token the JWT token
     * @return true if expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        try {
            return extractAllClaims(token).getExpiration().before(new Date());
        } catch (InvalidJwtException ex) {
            return true;
        }
    }

    /**
     * Get token expiration time
     *
     * @param token the JWT token
     * @return expiration time in milliseconds
     */
    public long getTokenExpirationTime(String token) {
        try {
            return extractAllClaims(token).getExpiration().getTime();
        } catch (InvalidJwtException ex) {
            return System.currentTimeMillis();
        }
    }

    /**
     * Create JWT token with claims
     */
    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
            .claims(claims)
            .subject(subject)
            .issuedAt(now)
            .expiration(expirationDate)
            .signWith(getSigningKey(), Jwts.SIG.HS256)
            .compact();
    }
}
