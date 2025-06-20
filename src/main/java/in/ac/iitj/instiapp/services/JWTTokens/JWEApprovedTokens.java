//package in.ac.iitj.instiapp.services.JWTTokens;
//
//
//import com.nimbusds.jose.JOSEException;
//import com.nimbusds.jwt.JWTClaimsSet;
//import org.springframework.security.authentication.CredentialsExpiredException;
//import org.springframework.stereotype.Service;
//
//import java.text.ParseException;
//import java.util.Optional;
//
//@Service
//public class JWEApprovedTokens extends JWEBaseClass {
//
//    public JWEApprovedTokens(JWEService jweService) {
//        super(jweService);
//    }
//
//
//    /**
//     * @param userName
//     * @param extraClaimsSet
//     * @return
//     */
//    public Optional<String> generateToken(String userName, JWTClaimsSet extraClaimsSet){
//
//        JWTClaimsSet.Builder jwtClaimsSetBuilder = new JWTClaimsSet.Builder(extraClaimsSet)
//                .claim(JWEConstants.KEYS_STATE, JWEConstants.STATES.STATE_APPROVED)
//                ;
//
//
//        try {
//            return Optional.of(jweService.encryptData(jwtClaimsSetBuilder.build(), userName, JWEConstants.ExpirationDuration.MEDIUM));
//        } catch (Exception e) {
//            return Optional.empty();
//        }
//
//    }
//
//
//
//    /**
//     * @param token
//     * @return Optional.empty() if the token is invalid
//     * @throws CredentialsExpiredException if the token is expired
//     */
//    public Optional<JWTClaimsSet> getClaimSet(String token){
//        try {
//            JWTClaimsSet claimSet = jweService.extractClaims(token);
//            if (jweService.isExpired(claimSet))
//                throw new CredentialsExpiredException("Expired JWT token for OAuth2 redirection");
//            return Optional.of(claimSet);
//        } catch (ParseException | JOSEException e) {
//            return Optional.empty();
//        }
//    }
//
//
//
//
//
//}