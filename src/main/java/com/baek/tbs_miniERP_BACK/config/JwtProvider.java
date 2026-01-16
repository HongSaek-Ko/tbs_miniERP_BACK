package com.baek.tbs_miniERP_BACK.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
public class JwtProvider {
    private final SecretKey key;
    private final long accessMinutes;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-minutes}") long accessMinutes
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessMinutes = accessMinutes;
    }

    public String createAccessToken(String userId, String username, List<String> perms) {
        Instant now = Instant.now();
        Instant exp = now.plus(accessMinutes, ChronoUnit.MINUTES);

        return Jwts.builder()
                .subject(userId)
                .claim("name", username)
                .claim("auth", perms) // ["PERM_ASSET_WRITE", ...]
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key)
                .compact();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims claims(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
}
