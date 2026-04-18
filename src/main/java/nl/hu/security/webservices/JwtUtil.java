package nl.hu.security.webservices;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {
    public static String generateToken(String username, UserRole role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, "this-is-a-512-bit-key-used-for-signing-jwt-tokens-that-should-be-changed-production".getBytes())
                .compact();
    }

    public static Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey("this-is-a-512-bit-key-used-for-signing-jwt-tokens-that-should-be-changed-production".getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getUsername(String token) {
        Claims claims = validateToken(token);
        return claims.getSubject();
    }

    public static UserRole getRole(String token) {
        Claims claims = validateToken(token);
        return UserRole.valueOf(claims.get("role", String.class));
    }
}
