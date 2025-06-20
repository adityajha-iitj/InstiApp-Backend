package in.ac.iitj.instiapp.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.config.JwtProvider;
import in.ac.iitj.instiapp.database.entities.User.Usertype;
import in.ac.iitj.instiapp.exception.OAuthTokenExchangeException;
import in.ac.iitj.instiapp.payload.Auth.AuthResponse;
import in.ac.iitj.instiapp.payload.Auth.SignupDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;


    @Autowired
    public AuthController(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    private String getOauthAccessTokenGoogle(String code) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var params = new org.springframework.util.LinkedMultiValueMap<String,String>();
        params.add("code", code);
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", googleRedirectUri);
        params.add("grant_type", "authorization_code");
        var req = new HttpEntity<>(params, headers);
        ResponseEntity<String> resp = rest.postForEntity(
                "https://oauth2.googleapis.com/token", req, String.class);
        if (!resp.getStatusCode().is2xxSuccessful()) {
            throw new OAuthTokenExchangeException("Failed to exchange Google code");
        }
        JsonObject obj = new Gson().fromJson(resp.getBody(), JsonObject.class);
        return obj.get("access_token").getAsString();
    }
    private JsonObject getProfileDetailsGoogle(String token) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        var req = new HttpEntity<>(headers);
        ResponseEntity<String> resp = rest.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET, req, String.class);
        return new Gson().fromJson(resp.getBody(), JsonObject.class);
    }




}
