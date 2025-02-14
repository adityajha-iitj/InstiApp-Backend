package in.ac.iitj.instiapp.services.JWTTokens;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Optional;

import static in.ac.iitj.instiapp.services.JWTTokens.JWEConstants.*;

@Service
public class JWEOAuth2Tokens extends JWEBaseClass {


    public JWEOAuth2Tokens(JWEService jweService) {
        super(jweService);
    }

    /**
     * @param oauth2AccessToken
     * @param oAuth2RefreshToken
     * @param deviceId
     * @param oAuth2User
     * @return a token if the generation is successful else empty
     * The token contains the following claims:
     * - {@link JWEConstants#KEYS_OAUTH2_ACCESS_TOKEN} <br>
     * - {@link JWEConstants#KEYS_OAUTH2_REFRESH_TOKEN} <br>
     * - {@link JWEConstants#KEYS_DEVICE_ID} <br>
     * - {@link JWEConstants#KEYS_STATE} <br>
     * - {@link JWEConstants#KEYS_EMAIL} <br>
     * - {@link JWEConstants#KEYS_NAME} <br>
     * - {@link JWEConstants#KEYS_AVATAR} <br>
     */
    public Optional<String> generateToken(String oauth2AccessToken, String oAuth2RefreshToken, String deviceId, OAuth2User oAuth2User) {
        JWTClaimsSet.Builder jwtClaimsSetBuilder = new JWTClaimsSet.Builder();

        JWTClaimsSet claimsSet = jwtClaimsSetBuilder.claim(KEYS_OAUTH2_ACCESS_TOKEN, oauth2AccessToken)
                .claim(KEYS_OAUTH2_REFRESH_TOKEN, oAuth2RefreshToken)
                .claim(KEYS_DEVICE_ID, deviceId)
                .claim(KEYS_STATE, STATES.STATE_PENDING)
                .claim(KEYS_EMAIL, oAuth2User.getAttribute("email"))
                .claim(KEYS_NAME, oAuth2User.getAttribute("name"))
                .claim(KEYS_AVATAR, oAuth2User.getAttribute("picture"))
                .build();

        try {
            return Optional.of(jweService.encryptData(claimsSet, oAuth2User.getAttribute("email"), ExpirationDuration.SHORT));
        } catch (ParseException | JOSEException e) {
            return Optional.empty();
        }

    }

    /**
     * @param token
     * @return Optional.empty() if the token is invalid
     * @throws CredentialsExpiredException if the token is expired
     */
    public Optional<JWTClaimsSet> getClaimSet(String token) {

        try {
            JWTClaimsSet jwtClaimsSet = jweService.extractClaims(token);
            if (jweService.isExpired(jwtClaimsSet))
                throw new CredentialsExpiredException("Expired JWT token for OAuth2 redirection");
            return Optional.of(jwtClaimsSet);
        } catch (ParseException | JOSEException e) {
            return Optional.empty();
        }
    }
}
