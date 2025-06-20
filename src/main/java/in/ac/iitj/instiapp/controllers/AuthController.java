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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

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

    // 1) Allow GET (so a browser logout link works) and POST
    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public void logout(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {

        // --- clear local session & Spring Security context ---
        if (request.getSession(false) != null) {
            request.getSession().invalidate();
        }
        SecurityContextHolder.clearContext();

        // --- clear your JWT cookies (always mark them Secure=true so they actually overwrite) ---
        clearCookie(response, "accessToken", "/");
        clearCookie(response, "refreshToken", "/");
        clearCookie(response, "JSESSIONID", "/");

        // 2) Redirect to your OAuth2 entry‑point WITH prompt=select_account
        response.sendRedirect("/oauth2/authorization/google?prompt=select_account");
    }

    // simplified clearCookie helper
    private void clearCookie(HttpServletResponse response,
                             String name,
                             String path) {
        Cookie c = new Cookie(name, "");
        c.setPath(path);
        c.setHttpOnly(true);
        c.setSecure(true);
        c.setMaxAge(0);
        response.addCookie(c);
        System.out.println("[Logout] Cleared cookie: " + name);
    }
}