package in.ac.iitj.instiapp.services.JWTTokens;

import com.nimbusds.jwt.JWTClaimsSet;
import in.ac.iitj.instiapp.Repository.JWTRefreshTokenRepository;
import in.ac.iitj.instiapp.Repository.OAuth2TokenRepository;
import in.ac.iitj.instiapp.Utils.CookieHelper;
import in.ac.iitj.instiapp.database.entities.Auth.JWTRefreshToken;
import in.ac.iitj.instiapp.database.entities.Auth.OAuth2Tokens;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.payload.Auth.SignupDto;
import in.ac.iitj.instiapp.services.UtilitiesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static in.ac.iitj.instiapp.services.JWTTokens.JWEConstants.*;

@Service
public class TokenService {


    private final JWEApprovedTokens jweApprovedTokens;

    private final UtilitiesService utilitiesService;
    private final JWTRefreshTokenRepository jwtRefreshTokenRepository;
    private final OAuth2TokenRepository oAuth2TokenRepository;


    public TokenService(JWEApprovedTokens jweApprovedTokens, UtilitiesService utilitiesService, JWTRefreshTokenRepository jwtRefreshTokenRepository, OAuth2TokenRepository oAuth2TokenRepository) {
        this.jweApprovedTokens = jweApprovedTokens;

        this.utilitiesService = utilitiesService;
        this.jwtRefreshTokenRepository = jwtRefreshTokenRepository;
        this.oAuth2TokenRepository = oAuth2TokenRepository;
    }
    // ------------------- Token methods ------------------


    /**
     * @param response
     * @param args - 0  -> username
     *             - 1 -> email
     *             - 2 -> name
     *             - 3 -> device_id
     *             - 4 -> phone_number
     *             - 5 -> user_type
     */

    public void generateAndSaveRefreshTokenToken(HttpServletResponse response, String[] args , Long userId, Pair<String , String> oauth2AccessTokenAndRefreshToken){

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .claim(KEYS_EMAIL, args[1])
                .claim(KEYS_NAME, args[2])
                .claim(KEYS_DEVICE_ID, args[3])
                .claim(KEYS_PHONE_NUMBER, args[4])
                .claim(KEYS_USER_TYPE, args[5])
                .build();

        Optional<String> approvedToken = jweApprovedTokens.generateToken(args[0],jwtClaimsSet);
        if (approvedToken.isEmpty())
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");


        System.out.println("Approved Token,Cookieissetting fsdljajfdslajf: " + approvedToken);

        CookieHelper.setAuthCookie(response, approvedToken.get(), ExpirationDuration.MEDIUM, CookieHelper.HEADER_SAMESITE_STRICT);


        String refreshToken = utilitiesService.generateRandomString(32);

        User u = new User(args[0]);
        u.setId(userId);

        jwtRefreshTokenRepository.save(new JWTRefreshToken(
                null, u, jwtClaimsSet.getClaim(KEYS_DEVICE_ID).toString(), refreshToken, Instant.ofEpochMilli(System.currentTimeMillis() + ExpirationDuration.LONG.getDuration())));


        OAuth2Tokens oAuth2Tokens = new OAuth2Tokens();
        oAuth2Tokens.setAccessToken(oauth2AccessTokenAndRefreshToken.getFirst());
        oAuth2Tokens.setRefreshToken(oauth2AccessTokenAndRefreshToken.getSecond());
        oAuth2Tokens.setDeviceId(args[3]);
        oAuth2Tokens.setUser(u);
        oAuth2TokenRepository.save(oAuth2Tokens);

        CookieHelper.setRefreshTokenCookie(response, refreshToken, CookieHelper.HEADER_SAMESITE_STRICT);

    }






    public void generateAndSaveTokens(JWEAuthenticationToken jweAuthenticationToken, SignupDto signupDto, Pair<String, Long> userNameAndUserId, HttpServletResponse response) {


        JWTClaimsSet jwtClaimsSet = (JWTClaimsSet) jweAuthenticationToken.getCredentials();



        generateAndSaveRefreshTokenToken(response, new String[]{userNameAndUserId.getFirst(),
                jwtClaimsSet.getClaim(KEYS_EMAIL).toString(),
                jwtClaimsSet.getClaim(KEYS_NAME).toString(),
                jwtClaimsSet.getClaim(KEYS_DEVICE_ID).toString(),
                signupDto.getPhoneNumber(),
                signupDto.getUserTypeName()},
                userNameAndUserId.getSecond(),
                Pair.of(jwtClaimsSet.getClaim(KEYS_OAUTH2_ACCESS_TOKEN).toString(), String.valueOf(jwtClaimsSet.getClaim(KEYS_OAUTH2_REFRESH_TOKEN)) ));

    }


    public void generateNewJweToken(JWEAuthenticationToken jweAuthenticationToken, HttpServletRequest req, HttpServletResponse response) {
        JWTClaimsSet jwtClaimsSet = (JWTClaimsSet) jweAuthenticationToken.getCredentials();
        String subject = jwtClaimsSet.getSubject();


        Map<String, Object> jwtRefreshToken = jwtRefreshTokenRepository.getRefreshTokenAndTokenExpireTimeByDeviceIdAndUserName(subject, jwtClaimsSet.getClaim(KEYS_DEVICE_ID).toString());

        if (jwtRefreshToken.isEmpty() ||
                ((Timestamp) jwtRefreshToken.get("expirationTime")).toInstant().toEpochMilli() < System.currentTimeMillis() ||
                !jwtRefreshToken.get("refreshToken").equals(CookieHelper.getRefreshTokenCookieValue(req).orElseThrow(() -> new BadCredentialsException("Refresh Token not valid or expired")))
        ) {

            CookieHelper.deleteAuthCookie(response);
            CookieHelper.deleteRefreshTokenCookie(response);
            return;
        }

        JWTClaimsSet jwtClaimsSet1 = new JWTClaimsSet.Builder(jwtClaimsSet).build();

        CookieHelper.setAuthCookie(response, jweApprovedTokens.generateToken(subject, jwtClaimsSet1).orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error")), ExpirationDuration.MEDIUM, CookieHelper.HEADER_SAMESITE_STRICT);
    }


    public void deleteRefreshToken(String userName, String deviceId) {
        jwtRefreshTokenRepository.deleteByUserNameAndDeviceId(userName, deviceId);
    }


}
