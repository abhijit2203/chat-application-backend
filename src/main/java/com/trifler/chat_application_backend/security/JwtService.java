package com.trifler.chat_application_backend.security;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	    private final String SECRET =
	            "mysecretkeymysecretkeymysecretkey";

	    private final long EXPIRATION =
	            1000L * 60 * 60; // 1 hour

	    // Convert secret into a signing key
	    private Key getSignKey() {
	        return Keys.hmacShaKeyFor(
	                SECRET.getBytes(java.nio.charset.StandardCharsets.UTF_8)
	        );
	    }

	    // Generate JWT token
	    public String generateToken(String email) {

	        Date now = new Date();
	        Date expirationDate =
	                new Date(now.getTime() + EXPIRATION);

	        return Jwts.builder()
	                .subject(email)
	                .issuedAt(now)
	                .expiration(expirationDate)
	                .signWith(getSignKey())
	                .compact();
	    }

	    // Extract email from JWT token
	    public String extractEmail(String token) {
	        return extractAllClaims(token).getSubject();
	    }

	    // Validate token
	    public boolean isTokenValid(String token, String email) {

	        Claims claims = extractAllClaims(token);

	        return claims.getSubject().equals(email)
	                && claims.getExpiration().after(new Date());
	    }

	    // Verify signature and extract token claims
	    private Claims extractAllClaims(String token) {

	        return Jwts.parser()
	                .verifyWith((javax.crypto.SecretKey) getSignKey())
	                .build()
	                .parseSignedClaims(token)
	                .getPayload();
	    }
	
	

}
