//package in.ac.iitj.instiapp.services.JWTTokens;
//
//import com.nimbusds.jose.JOSEException;
//import com.nimbusds.jwt.JWTClaimsSet;
//import org.springframework.security.authentication.CredentialsExpiredException;
//import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
//import org.springframework.stereotype.Service;
//
//import java.text.ParseException;
//import java.util.Optional;
//import java.util.Set;
//
//import static in.ac.iitj.instiapp.services.JWTTokens.JWEConstants.KEYS_STATE;
//import static in.ac.iitj.instiapp.services.JWTTokens.JWEConstants.STATES.STATE_UNREGISTERED;
//
//@Service
//public class JWEOauth2Redirection extends JWEBaseClass {
//
//
//    public static final String KEYS_OAUTH2AUTHORIZATION_REQUEST_CLIENT_ID = "clientId";
//    public static final String KEYS_OAUTH2AUTHORIZATION_REQUEST_REDIRECT_URI = "redirectUri";
//    public static final String KEYS_OAUTH2AUTHORIZATION_REQUEST_SCOPES = "scopes";
//    public static final String KEYS_OAUTH2AUTHORIZATION_REQUEST_STATE = "reqState";
//    public static final String KEYS_OAUTH2AUTHORIZATION_REQUEST_AUTHORIZATION_URI = "authorizationUri";
//    public static final String KEYS_OAUTH2AUTHORIZATION_REQUEST_ATTRIBUTES = "attributes";
//
//
//    public JWEOauth2Redirection(JWEService jweService) {
//        super(jweService);
//    }
//
//
//    /**
//     * @param oAuth2AuthorizationRequest
//     * @return empty if the token generation fails or deviceId is null
//     */
//    public Optional<String> generateToken(OAuth2AuthorizationRequest oAuth2AuthorizationRequest) {
//        JWTClaimsSet.Builder jwtClaimsSet = new JWTClaimsSet.Builder();
//
//
//        jwtClaimsSet.claim(KEYS_OAUTH2AUTHORIZATION_REQUEST_REDIRECT_URI, oAuth2AuthorizationRequest.getRedirectUri())
//                .claim(KEYS_OAUTH2AUTHORIZATION_REQUEST_CLIENT_ID, oAuth2AuthorizationRequest.getClientId())
//                .claim(KEYS_OAUTH2AUTHORIZATION_REQUEST_SCOPES, String.join(",", oAuth2AuthorizationRequest.getScopes()))
//                .claim(KEYS_OAUTH2AUTHORIZATION_REQUEST_STATE, oAuth2AuthorizationRequest.getState())
//                .claim(KEYS_OAUTH2AUTHORIZATION_REQUEST_AUTHORIZATION_URI, oAuth2AuthorizationRequest.getAuthorizationUri())
//                .claim(KEYS_OAUTH2AUTHORIZATION_REQUEST_ATTRIBUTES, oAuth2AuthorizationRequest.getAttributes())
//
//
//                .claim(KEYS_STATE, STATE_UNREGISTERED)
//
//        ;
//
//        System.out.println(jwtClaimsSet.getClaims());
//
//
//        try {
//            return Optional.of(jweService.encryptData(jwtClaimsSet.build(), oAuth2AuthorizationRequest.getState(), JWEConstants.ExpirationDuration.VERY_SHORT));
//        } catch (ParseException | JOSEException e) {
//            return Optional.empty();
//        }
//    }
//
//    /**
//     * @param jwtClaimsSet
//     * @return Optional.empty() if the token is invalid
//     * @throws CredentialsExpiredException if the token is expired
//     */
//    public Optional<OAuth2AuthorizationRequest> getPayload(JWTClaimsSet jwtClaimsSet) {
//        try {
//
//
//            if (jweService.isExpired(jwtClaimsSet))
//                throw new CredentialsExpiredException("Expired JWT token for OAuth2 redirection");
//
//
//            return Optional.of(OAuth2AuthorizationRequest.authorizationCode()
//                    .clientId(jwtClaimsSet.getStringClaim(KEYS_OAUTH2AUTHORIZATION_REQUEST_CLIENT_ID))
//                    .redirectUri(jwtClaimsSet.getStringClaim(KEYS_OAUTH2AUTHORIZATION_REQUEST_REDIRECT_URI))
//                    .state(jwtClaimsSet.getStringClaim(KEYS_OAUTH2AUTHORIZATION_REQUEST_STATE))
//                    .authorizationUri(jwtClaimsSet.getStringClaim(KEYS_OAUTH2AUTHORIZATION_REQUEST_AUTHORIZATION_URI))
//                    .attributes(jwtClaimsSet.getJSONObjectClaim(KEYS_OAUTH2AUTHORIZATION_REQUEST_ATTRIBUTES))
//                    .scopes(Set.of(jwtClaimsSet.getStringClaim(KEYS_OAUTH2AUTHORIZATION_REQUEST_SCOPES).split(",")))
//                    .build()
//            );
//
//        } catch (ParseException | JOSEException e) {
//            return Optional.empty();
//        }
//    }
//
//    public Optional<JWTClaimsSet> getJWTClaimsSet(String token) {
//        try {
//            return Optional.of(jweService.decryptToken(token));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Optional.empty();
//        }
//    }
//
//
//
//}
