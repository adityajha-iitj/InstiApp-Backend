package in.ac.iitj.instiapp.controllers;
import com.nimbusds.jwt.JWTClaimsSet;
import in.ac.iitj.instiapp.Utils.CookieHelper;
import in.ac.iitj.instiapp.authfiles.PreAuthoriseFilters;
import in.ac.iitj.instiapp.payload.Auth.SignupDto;
import in.ac.iitj.instiapp.services.JWTTokens.JWEAuthenticationToken;
import in.ac.iitj.instiapp.services.JWTTokens.JWEConstants;
import in.ac.iitj.instiapp.services.JWTTokens.TokenService;
import in.ac.iitj.instiapp.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping
public class AuthController {

    private final UserService userService;

    private final TokenService tokenService;
    @Autowired
    public AuthController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService  = tokenService;
    }


    @PreAuthoriseFilters.HasPendingState
    @PostMapping("/api/v1/auth/signup")
    public ResponseEntity<?> signup(HttpServletResponse response, Authentication authentication, @RequestBody SignupDto signupDto) {

        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        JWEAuthenticationToken jweAuthenticationToken = (JWEAuthenticationToken) authentication;


        if (jweAuthenticationToken.getState().equals(JWEConstants.STATES.STATE_PENDING)) {

            JWTClaimsSet jwtClaimsSet = (JWTClaimsSet) jweAuthenticationToken.getCredentials();
            Pair<String , Long> userNameAndUserId = userService.save(signupDto, jwtClaimsSet);


            tokenService.generateAndSaveTokens(jweAuthenticationToken, signupDto, userNameAndUserId, response);

            return new ResponseEntity<>( HttpStatus.CREATED);
        }


        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");


    }


    @PreAuthoriseFilters.HasApprovedState
    @GetMapping("/home")
    public ResponseEntity<?> getHome() {
        return new ResponseEntity<>("Hello World", HttpStatus.OK);
    }


    @GetMapping("api/v1/auth/logout")
    public ResponseEntity<?> logOut(HttpServletResponse httpServletResponse) {
        CookieHelper.deleteAuthCookie(httpServletResponse);
        CookieHelper.deleteRefreshTokenCookie(httpServletResponse);
        CookieHelper.deleteJSessionIdCookie(httpServletResponse);
        return new ResponseEntity<>("Logged Out Successfully", HttpStatus.OK);
    }

    @GetMapping("${server.error.path:/error}")
    public ResponseEntity<?> getError() {
        return new ResponseEntity<>("Error page", HttpStatus.OK);
    }


    @PreAuthoriseFilters.HasApprovedState
    @GetMapping("/api/v1/auth/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {


        JWEAuthenticationToken jweAuthenticationToken = (JWEAuthenticationToken) authentication;

        if (jweAuthenticationToken.getState().equals(JWEConstants.STATES.STATE_APPROVED)) {
            tokenService.generateNewJweToken(jweAuthenticationToken,request, response);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");

    }

    @GetMapping("/api/v1/auth/status")
    public ResponseEntity<?> getStatus(Authentication authentication){
        if(authentication instanceof  JWEAuthenticationToken jweAuthenticationToken){
            return  new ResponseEntity<>(jweAuthenticationToken.getState().toString(), HttpStatus.OK);
        }
        return new ResponseEntity<>("STATE_ANONYMOUS", HttpStatus.OK);
    }




}



