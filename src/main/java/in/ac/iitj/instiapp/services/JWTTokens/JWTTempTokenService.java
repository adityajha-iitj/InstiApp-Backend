package in.ac.iitj.instiapp.services.JWTTokens;


import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTTempTokenService {

    private final JWTService jwtService;

    @Value("${app.jwt.tempExpiration}")
    public static   Long EXPIRATION_TIME  ;

    @Autowired
    public JWTTempTokenService(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * @assumptions all the values in the claims exist and are not null
     * @param oauth2AccessToken  shouldn't be null
     * @param oauth2RefreshToken shouldn't be null
     * @param deviceId           shouldn't be null
     * @param otherClaims        - should contain three specific keys - email, name, avatar
     * @return a jwtToken encoding the above values while setting a appropriate expiration time
     */
    public String GenerateToken(String oauth2AccessToken, String oauth2RefreshToken, String deviceId, Map<String, Object> otherClaims) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("oauth2AccessToken", oauth2AccessToken);
        claims.put("oauth2RefreshToken", oauth2RefreshToken);
        claims.put("deviceId", deviceId);
        claims.put("state", JWTService.STATES.STATE_PENDING);



        // To add more parameters they could be added here
        claims.put("email", otherClaims.get("email"));
        claims.put("name", otherClaims.get("name"));
        claims.put("avatar", otherClaims.get("avatar"));

        return jwtService.GenerateToken(otherClaims.get("email").toString(), claims,
                new Date(System.currentTimeMillis()), EXPIRATION_TIME);
    }

    /**
     * @param token
     * @return False only if token is expired
     */
    public Boolean validateToken(String token) {
        return jwtService.validateToken(token, jwtService.extractPayload(token));
    }


    /**
     * @param token
     * @return a new token in which state is VERIFIED and updates the expiration time.
     * @throws BadCredentialsException
     */
    public String updateTokenStateToVerified(String token) throws BadCredentialsException {
        if(!validateToken(token)) {
          throw new BadCredentialsException("Invalid token");
        }

        Claims claims = jwtService.extractAllClaims(token);
        claims.put("state", JWTService.STATES.STATE_VERIFIED);

        return jwtService.GenerateToken(claims.get("email").toString(), claims,
                new Date(System.currentTimeMillis()), EXPIRATION_TIME);
    }

    public JWTService.STATES getTokenState(String token) {
        return jwtService.extractClaim(token, (claims) ->  (JWTService.STATES) claims.get("state"));
    }

    public Claims getClaims(String token) {
        return jwtService.extractAllClaims(token);
    }

    public String getPayload(String token) {
        return jwtService.extractPayload(token);
    }
}
