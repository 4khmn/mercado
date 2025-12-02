package com.example.market.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    private final SecretKey secretKey = Keys.hmacShaKeyFor("verysecretkeywhichshouldbereplaced1234567890".getBytes());

    private final long jwtExpirationMs = 60 * 60 * 1000;

    public String generateToken(String username, String rolesStr) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", rolesStr)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getRolesFromToken(String token) {
        return getClaimFromToken(token, c -> c.get("roles", String.class));
    }

    public boolean validateToken(String token, String username) {
        final String usernameFromToken = getUsernameFromToken(token);
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return (usernameFromToken.equals(username) && !expiration.before(new Date()));
    }
}
