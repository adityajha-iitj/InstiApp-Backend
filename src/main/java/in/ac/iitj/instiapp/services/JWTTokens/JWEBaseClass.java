//package in.ac.iitj.instiapp.services.JWTTokens;
//
//
//import com.nimbusds.jwt.JWTClaimsSet;
//
//import java.util.Optional;
//
//public abstract class JWEBaseClass {
//
//
//    protected final JWEService jweService;
//
//
//
//
//    public JWEBaseClass(JWEService jweService) {
//        this.jweService = jweService;
//    }
//
//    /**
//     * @param jweToken
//     * @return empty if the token is invalid or the claimset is null
//     */
//    public Optional<JWTClaimsSet> extractClaims(String jweToken) {
//        try {
//            return Optional.of(jweService.extractClaims(jweToken));
//        } catch (Exception e) {
//            return Optional.empty();
//        }
//    }
//
//    /**
//     * @param jwtClaimsSet
//     * @return false if the token is invalid else the output
//     */
//    public boolean validateExpiration(JWTClaimsSet jwtClaimsSet) {
//
//        try {
//            return jweService.isExpired(jwtClaimsSet);
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//}