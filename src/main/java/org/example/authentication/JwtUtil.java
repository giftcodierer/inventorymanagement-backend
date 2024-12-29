package org.example.authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.enterprise.context.ApplicationScoped;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class JwtUtil {
    private String secret = "your_secret_key";

    public String generateToken(String username, String role, Long userID) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("userID", userID);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String extractRole(String token) {
        return (String) Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("role");
    }

    public Long extractUserID(String token) {
        return ((Number) Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("userID")).longValue();
    }

    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }
}