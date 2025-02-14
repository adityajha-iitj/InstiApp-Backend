package in.ac.iitj.instiapp.services.JWTTokens;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;

@Service
public class JWEService {

    // Reusable header objects
    private static final JWSHeader DEFAULT_JWS_HEADER = new JWSHeader(JWSAlgorithm.RS256);
    private static final JWEHeader DEFAULT_JWE_HEADER = new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM)
            .contentType("JWT")
            .build();
    private RSADecrypter rsaDecrypter;
    private RSAEncrypter rsaEncrypter;
    private JWSSigner jwsSigner;

    @PostConstruct
    private void init() {
        try {
            KeyPair keyPair = generateRSAKeyPair();
            jwsSigner = new RSASSASigner(keyPair.getPrivate());
            rsaDecrypter = new RSADecrypter(keyPair.getPrivate());
            rsaEncrypter = new RSAEncrypter((RSAPublicKey) keyPair.getPublic());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating key pair", e);
        }
    }

    private KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    /**
     * Encrypts a JWT containing the given claims. The subject is updated to the provided value
     * and the token expiration is set to 60 seconds from now.
     *
     * @param claimsSet the base claims set, can be null
     * @param subject   the subject to be set in the claims, if null returns null
     * @return the serialized encrypted token
     * @throws Exception if encryption fails
     */
    public String encryptData(JWTClaimsSet claimsSet, String subject, JWEConstants.ExpirationDuration expirationDuration) throws ParseException, JOSEException {

        if (subject == null) return null;
        claimsSet = claimsSet == null ? new JWTClaimsSet.Builder().build() : claimsSet;

        // Build a new claims set with updated subject and expiration time.
        JWTClaimsSet updatedClaimsSet = new JWTClaimsSet.Builder(claimsSet)
                .issuer("instiapp")
                .subject(subject)
                .expirationTime(new Date(System.currentTimeMillis() + expirationDuration.getDuration()))
                .build();

        SignedJWT signedJWT = new SignedJWT(DEFAULT_JWS_HEADER, updatedClaimsSet);
        signedJWT.sign(jwsSigner);
        JWEObject jweObject = new JWEObject(DEFAULT_JWE_HEADER, new Payload(signedJWT));
        jweObject.encrypt(rsaEncrypter);
        return jweObject.serialize();
    }

    /**
     * Decrypts the provided JWE token and returns its claims after validating expiration.
     *
     * @param jweToken the encrypted token
     * @return the JWTClaimsSet contained in the token
     * @throws Exception if decryption fails or the token is expired
     */
    public JWTClaimsSet extractClaims(String jweToken) throws ParseException, JOSEException {
        JWTClaimsSet claimsSet = getSignedJWT(jweToken).getJWTClaimsSet();
        return claimsSet;
    }

    // Consolidates the decryption logic to avoid duplication.
    private SignedJWT getSignedJWT(String jweToken) throws ParseException, JOSEException {
        JWEObject jweObject = JWEObject.parse(jweToken);
        jweObject.decrypt(rsaDecrypter);
        return jweObject.getPayload().toSignedJWT();
    }

    public boolean isExpired(JWTClaimsSet claimsSet) throws ParseException, JOSEException {
        return claimsSet.
                getExpirationTime().
                getTime() < System.currentTimeMillis();
    }




}
