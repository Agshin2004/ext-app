package com.agshin.extapp.utils;

import com.agshin.extapp.model.entities.User;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
    private final SecretKey secretKey;
    private final Long jwtExpirationMs;

    public JwtUtils(@Value("${app.jwt.secret}") String secret,
                    @Value("${app.jwt.refreshExpiration:36000000}") Long jwtExpirationMs
    ) {
        this.secretKey = getSigningKey(secret);
        this.jwtExpirationMs = jwtExpirationMs;
    }

    private SecretKey getSigningKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(@NotNull User user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put(User.USER_ID, user.getId());
        claims.put(User.USERNAME, user.getUsername());
        claims.put(User.EMAIL, user.getEmail());
        claims.put(User.ROLE, user.getRole());
        claims.put(User.REGISTRATION_STATUS, user.getRole());

        return createToken(user.getEmail(), claims);
    }

    private String createToken(String subject, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // email
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String email = extractEmail(token);
        System.out.println(email);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);

    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiration.before(new Date());
    }
}
