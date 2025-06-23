package in.ac.iitj.instiapp.controllers;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import in.ac.iitj.instiapp.config.JwtProvider;
import in.ac.iitj.instiapp.payload.Auth.SignupDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import in.ac.iitj.instiapp.database.entities.User.User;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping("/getUser")
    public ResponseEntity<UserBaseDto> getUser(@RequestHeader("Authorization") String jwt) {
        String userName = jwtProvider.getUsernameFromToken(jwt);
        return ResponseEntity.ok(userService.getUserLimited(userName));
    }

}
