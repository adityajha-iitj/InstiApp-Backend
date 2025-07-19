package in.ac.iitj.instiapp.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.config.JwtProvider;
import in.ac.iitj.instiapp.database.entities.User.User; // Import your User entity
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // This is key
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

    private final GoogleIdTokenVerifier verifier;

    @Autowired
    public AuthController(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, @Value("${spring.security.oauth2.client.registration.google.client-id}") String googleClientId) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.googleClientId = googleClientId;
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public void logout(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {

        if (request.getSession(false) != null) {
            request.getSession().invalidate();
        }
        SecurityContextHolder.clearContext();

        clearCookie(response, "accessToken", "/");
        clearCookie(response, "refreshToken", "/");
        clearCookie(response, "JSESSIONID", "/");

        response.sendRedirect("/oauth2/authorization/google?prompt=select_account");
    }

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

    @PostMapping("/google/signin")
    public ResponseEntity<?> googleSignIn(@RequestBody Map<String, String> requestBody) {
        String idTokenString = requestBody.get("token");

        if (idTokenString == null || idTokenString.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Missing Google ID token"));
        }

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid Google token"));
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            String googleId = payload.getSubject();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");

            // Use UserService to find or create the user
            User user = userService.findOrCreateGoogleUser(googleId, email, name, pictureUrl);

            // --- Key change here: Create an Authentication object from the User details ---
            List<GrantedAuthority> authorities = new ArrayList<>();
            // Assuming Usertype.getName() returns a role string like "ROLE_USER"
            if (user.getUserType() != null) {
                authorities.add(new SimpleGrantedAuthority(user.getUserType().getName()));
            } else {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // Fallback if Usertype is null
            }

            // Create an Authentication object for the JWT Provider
            // The first argument is the principal (username/email), second is credentials (null for JWT), third are authorities
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUserName(), null, authorities);

            // Set this Authentication object in the SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Now, use your existing JWT Provider methods with this Authentication object
            String accessToken = jwtProvider.generateAccessToken(authentication);
            String refreshToken = jwtProvider.generateRefreshToken(authentication);

            Map<String, String> responseData = new HashMap<>();
            responseData.put("accessToken", accessToken);
            responseData.put("refreshToken", refreshToken);

            return ResponseEntity.ok(responseData);

        } catch (Exception e) {
            System.err.println("Google sign-in error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid Google token or user processing failed"));
        }
    }
}