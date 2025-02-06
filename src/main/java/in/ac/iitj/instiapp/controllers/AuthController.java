package in.ac.iitj.instiapp.controllers;

import in.ac.iitj.instiapp.payload.Auth.SignupDto;
import in.ac.iitj.instiapp.services.JWTTokens.JWTApprovedTokenService;
import in.ac.iitj.instiapp.services.JWTTokens.JWTTempTokenService;
import in.ac.iitj.instiapp.services.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;

@RestController("api/v1/auth")
public class AuthController {

    private final JWTTempTokenService jwtTempTokenService;
    private final UserService userService;
    private final JWTApprovedTokenService jwtApprovedTokenService;

    @Autowired
    public AuthController(JWTTempTokenService jwtTempTokenService, UserService userService, JWTApprovedTokenService jwtApprovedTokenService) {
        this.jwtTempTokenService = jwtTempTokenService;
        this.userService = userService;
        this.jwtApprovedTokenService = jwtApprovedTokenService;
    }

    @PostMapping
    public ResponseEntity<?> signup(HttpServletRequest request, HttpServletResponse response, Authentication authentication, @RequestBody SignupDto signupDto ){
        if(authentication != null){
            Authentication auth = (PreAuthenticatedAuthenticationToken) authentication;

            Claims claim  = jwtTempTokenService.getClaims(authentication.getCredentials().toString());

            String userName = userService.save(signupDto, claim);

            HashMap<String, Object> approvedClaims = new HashMap<>();
            approvedClaims.put("deviceId", claim.get("deviceId").toString());
            approvedClaims.put("userName",userName);
            approvedClaims.put("email", claim.get("email").toString());
            approvedClaims.put("phoneNumber", signupDto.getPhoneNumber());
            approvedClaims.put("userType", signupDto.getUserTypeName());
            approvedClaims.put("organisationRoleSet", Collections.emptySet());


            String approvedToken = jwtApprovedTokenService.GenerateToken(userName, approvedClaims);


            Cookie cookie = new Cookie("Authorization", String.format("Bearer %s",approvedToken));
            cookie.setMaxAge(jwtApprovedTokenService.getEXPIRATION_TIME());
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            return  new ResponseEntity<>(HttpStatus.CREATED);
        }

        return  new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


}
