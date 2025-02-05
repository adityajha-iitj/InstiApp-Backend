package in.ac.iitj.instiapp.controllers;

import in.ac.iitj.instiapp.services.JWTTokens.JWTTempTokenService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController("api/v1/auth")
public class AuthController {

    private final JWTTempTokenService jwtTempTokenService;

    @Autowired
    public AuthController(JWTTempTokenService jwtTempTokenService) {
        this.jwtTempTokenService = jwtTempTokenService;
    }

        @PostMapping
    // TODO -  Take usercreatedto and other values
    public ResponseEntity<?> signup(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        if(authentication != null){
            Authentication auth = (PreAuthenticatedAuthenticationToken) authentication;

            Claims claim  = jwtTempTokenService.getClaims(authentication.getCredentials().toString());



//            TODO
            //store the tokens in the database from claims and other info from the appropriate model


            // store the information in the form of cookie and return it to the user in the form of cookie

//
//            response.addCookie(new Cookie("Authorization", ));

            return  new ResponseEntity<>(HttpStatus.OK);
        }

        return  new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


}
