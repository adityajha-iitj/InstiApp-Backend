package in.ac.iitj.instiapp.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtProvider {

    SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    private final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 3; // 3 days
    private final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 15; // 15 days

    public String generateAccessToken(Authentication auth) {
        List<String> authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + ACCESS_TOKEN_EXPIRATION))
                .claim("email", auth.getName())
                .claim("authorities", authorities)
                .claim("token_type", "access") // Added token type claim
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(Authentication auth) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + REFRESH_TOKEN_EXPIRATION))
                .claim("email", auth.getName())
                .claim("token_type", "refresh") // Added token type claim
                .signWith(key)
                .compact();
    }

    // FIXED: Removed substring operation
    public String getEmailFromToken(String jwt) {
        Claims claims = parseToken(jwt);
        return String.valueOf(claims.get("email"));
    }

    public String getEmailFromRefreshToken(String jwt) {
        Claims claims = parseToken(jwt);
        return String.valueOf(claims.get("email"));
    }

    public boolean validateAccessToken(String jwt) {
        try {
            Claims claims = parseToken(jwt);
            System.out.println("Token expiry: " + claims.getExpiration());
            System.out.println("Current time: " + new Date());
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            System.out.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    // FIXED: Added token type validation
    public boolean validateRefreshToken(String jwt) {
        try {
            Claims claims = parseToken(jwt);
            return "refresh".equals(claims.get("token_type"));
        } catch (Exception e) {
            System.out.println("Refresh token validation failed: " + e.getMessage());
            return false;
        }
    }

    // FIXED: Unified token parsing
    private Claims parseToken(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}