package in.ac.iitj.instiapp.services.JWTTokens;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTService {

    public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";

    public enum STATES{

        STATE_PENDING, // No phone numbers are verified only acessToken and refreshToken from OAuth2 are generated
        STATE_VERIFIED, // Phone numbers are verified but still in incomplete state
        STATE_APPROVED; // All information is completely there
    }


    public String extractPayload(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public   Claims extractAllClaims(String token) {
      return   Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
      }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, String newPayload){
        final String payload = extractPayload(token);
        return (payload.equals(newPayload) && !isTokenExpired(token));
    }


    public String GenerateToken(String payload, Map<String, Object> claims, Date issuedAt, Long ExpirationPeriod) {
        return createToken(claims, payload, issuedAt, ExpirationPeriod);

    }


    private String createToken(Map<String, Object> claims, String payload,Date issuedAt, Long ExpirationPeriod) {


        return Jwts.builder()
                .addClaims(claims)
                .setSubject(payload)
                .setIssuedAt(issuedAt)
                .setExpiration(new Date(issuedAt.getTime() + ExpirationPeriod))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}