//package in.ac.iitj.instiapp.services.JWTTokens;
//
//import com.nimbusds.jose.*;
//import com.nimbusds.jose.crypto.RSADecrypter;
//import com.nimbusds.jose.crypto.RSAEncrypter;
//import com.nimbusds.jose.crypto.RSASSASigner;
//import com.nimbusds.jwt.JWTClaimsSet;
//import com.nimbusds.jwt.SignedJWT;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.security.*;
//import java.security.interfaces.RSAPublicKey;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//import java.text.ParseException;
//import java.util.Base64;
//import java.util.Date;
//
//
//@Service
//public class JWEService {
//
//    private static final JWSHeader DEFAULT_JWS_HEADER = new JWSHeader(JWSAlgorithm.RS256);
//    private static final JWEHeader DEFAULT_JWE_HEADER =
//            new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM)
//                    .contentType("JWT")
//                    .build();
//
//
//    private RSADecrypter rsaDecrypter;
//    private RSAEncrypter rsaEncrypter;
//    private JWSSigner jwsSigner;
//
//    @Value("${jwe.private-key}")
//    private String base64PrivateKey;
//
//    @Value("${jwe.public-key}")
//    private String base64PublicKey;
//
//    @PostConstruct
//    private void init() {
//        try {
//            if (base64PrivateKey == null || base64PublicKey == null) {
//                throw new IllegalStateException("❌ RSA keys not found in environment or properties");
//            }
//
//            // 🔐 Decode clean base64-encoded DER key bytes
//            byte[] privBytes = Base64.getDecoder().decode(base64PrivateKey.trim());
//            byte[] pubBytes = Base64.getDecoder().decode(base64PublicKey.trim());
//
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//
//            // 🔧 Rebuild private and public keys
//            PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privBytes));
//            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(pubBytes));
//
//            // 🛡️ Set up JWE and JWS logic
//            this.jwsSigner = new RSASSASigner(privateKey);
//            this.rsaDecrypter = new RSADecrypter(privateKey);
//            this.rsaEncrypter = new RSAEncrypter((RSAPublicKey) publicKey);
//
//            System.out.println("✅ RSA keys successfully initialized");
//
//        } catch (Exception e) {
//            System.err.println("❌ Error initializing RSA keys: " + e.getMessage());
//            throw new RuntimeException("❌ Failed to load RSA keys", e);
//        }
//    }
//
//
//
//    private KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
//        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
//        keyGen.initialize(2048);
//        return keyGen.generateKeyPair();
//    }
//
//    /**
//     * Encrypts a JWT containing the given claims. The subject is updated to the provided value
//     * and the token expiration is set to 60 seconds from now.
//     *
//     * @param claimsSet the base claims set, can be null
//     * @param subject   the subject to be set in the claims, if null returns null
//     * @return the serialized encrypted token
//     * @throws Exception if encryption fails
//     */
//    public String encryptData(JWTClaimsSet claimsSet, String subject, JWEConstants.ExpirationDuration expirationDuration) throws ParseException, JOSEException {
//
//        if (subject == null) return null;
//        claimsSet = claimsSet == null ? new JWTClaimsSet.Builder().build() : claimsSet;
//
//        // Build a new claims set with updated subject and expiration time.
//        JWTClaimsSet updatedClaimsSet = new JWTClaimsSet.Builder(claimsSet)
//                .issuer("instiapp")
//                .subject(subject)
//                .expirationTime(new Date(System.currentTimeMillis() + expirationDuration.getDuration()))
//                .build();
//
//        SignedJWT signedJWT = new SignedJWT(DEFAULT_JWS_HEADER, updatedClaimsSet);
//        signedJWT.sign(jwsSigner);
//        JWEObject jweObject = new JWEObject(DEFAULT_JWE_HEADER, new Payload(signedJWT));
//        jweObject.encrypt(rsaEncrypter);
//        return jweObject.serialize();
//    }
//
//    /**
//     * Decrypts the provided JWE token and returns its claims after validating expiration.
//     *
//     * @param jweToken the encrypted token
//     * @return the JWTClaimsSet contained in the token
//     * @throws Exception if decryption fails or the token is expired
//     */
//    public JWTClaimsSet extractClaims(String jweToken) throws ParseException, JOSEException {
//        JWTClaimsSet claimsSet = getSignedJWT(jweToken).getJWTClaimsSet();
//        return claimsSet;
//    }
//
//    // Consolidates the decryption logic to avoid duplication.
//    private SignedJWT getSignedJWT(String jweToken) throws ParseException, JOSEException {
//        JWEObject jweObject = JWEObject.parse(jweToken);
//        jweObject.decrypt(rsaDecrypter);
//        return jweObject.getPayload().toSignedJWT();
//    }
//
//    public boolean isExpired(JWTClaimsSet claimsSet) throws ParseException, JOSEException {
//        return claimsSet.
//                getExpirationTime().
//                getTime() < System.currentTimeMillis();
//    }
//
//
//
//
//}
