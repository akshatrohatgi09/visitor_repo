package com.police.evisitor.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtility implements Serializable {

    private static final long serialVersionUID = 234234523523L;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.secret.expiration}")
    private long jwtTokenValidity;

    private Key signingKey;

    @PostConstruct
    public void init() {
        signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateTokenBySSOId(String loginId) {
        return generateToken(loginId);
    }

    public String generateTokenByMobileNumber(String mobileNumber) {
        return generateToken(mobileNumber);
    }

    private String generateToken(String subject) {

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidity))
               .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String decodeData(String encodedData) {
        return new String(Base64.decodeBase64(encodedData), StandardCharsets.UTF_8);
    }
    
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token))
                && !isTokenExpired(token);
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}