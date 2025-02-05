package in.ac.iitj.instiapp.services.JWTTokens;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTApprovedTokenService {

    private final JWTService jwtService;

    @Value("${app.jwt.approvedExpiration}")
    private Long EXPIRATION_TIME;


    @Autowired
    public JWTApprovedTokenService(JWTService jwtService) {
        this.jwtService = jwtService;
    }


    /**
     * @param userName
     * @param claims deviceId, userName, email, phoneNumber, userType, organisationRoleSet keys are present with not null values
     * @return
     */
    public String GenerateToken(String userName, Map<String, Object> claims) {
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("deviceId", claims.get("deviceId"));
        claimsMap.put("userName", userName);
        claimsMap.put("email", claims.get("email"));
        claimsMap.put("phoneNumber", claims.get("phoneNumber"));
        claimsMap.put("userType", claims.get("userType"));
        claimsMap.put("organisationRoleSet", claims.get("organisationRoleSet"));
        claimsMap.put("state", JWTService.STATES.STATE_APPROVED);

        return jwtService.GenerateToken(userName, claimsMap,
                new Date(System.currentTimeMillis()),EXPIRATION_TIME);
    }

    public Boolean isTokenValid(String token, String payload) {
        return jwtService.validateToken(token, payload);
    }

    public Boolean isTokenExpired(String token) {
        return jwtService.isTokenExpired(token);
    }

    public Map<String, Object> getUsernameAndDeviceId(String token) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("userName", jwtService.extractClaim(token, (claims1 -> claims1.get("userName").toString())));
        claims.put("deviceId", jwtService.extractClaim(token, (claims1 -> claims1.get("deviceId").toString())));

        return claims;
    }

    public Claims getClaimsFromToken(String token) {
        return jwtService.extractAllClaims(token);
    }

}
