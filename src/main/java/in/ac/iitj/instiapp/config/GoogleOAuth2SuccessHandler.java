package in.ac.iitj.instiapp.config;

import in.ac.iitj.instiapp.database.entities.User.Usertype;
import in.ac.iitj.instiapp.payload.Auth.SignupDto;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import in.ac.iitj.instiapp.services.UserService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.oauth2.web-redirect-url:/}")
    private String webRedirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, java.io.IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String firstName = oauthUser.getAttribute("given_name");
        String lastName = oauthUser.getAttribute("family_name");

        System.out.println("[OAuth2] Email: " + email);
        System.out.println("[OAuth2] First Name: " + firstName);
        System.out.println("[OAuth2] Last Name: " + lastName);

        Long userExists = userService.emailExists(email);
        System.out.println("[OAuth2] User exists check returned: " + userExists);

        Usertype userType = new Usertype();
        userType.setName("Student");
        userType.setId(1L);

        boolean isNew = false;

        if (userExists == -1L) {
            System.out.println("[OAuth2] No existing user. Proceeding with signup...");

            String generatedUsername = userService.createUsername(firstName, lastName, email);
            System.out.println("[OAuth2] Generated Username: " + generatedUsername);

            SignupDto newUser = new SignupDto(
                    firstName,
                    email,
                    generatedUsername,
                    UUID.randomUUID().toString(),
                    "",
                    userType
            );

            try {
                userService.save(newUser);
                System.out.println("[OAuth2] New user saved successfully.");
            } catch (Exception e) {
                System.err.println("[OAuth2] Error saving new user: " + e.getMessage());
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "User creation failed.");
                return;
            }

            isNew = true;
        } else {
            System.out.println("[OAuth2] Existing user found. Skipping signup.");
        }

        String username = userService.getUsernameFromEmail(email);
        System.out.println("[OAuth2] Resolved username from email: " + username);

        UserDetailedDto userDetailedDto = userService.getUserDetailed(username);
        System.out.println("[OAuth2] Retrieved UserDetailedDto for login.");

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(userDetailedDto.getUserTypeName())
        );
        String password = userDetailedDto.getPassword() != null ? userDetailedDto.getPassword() : "";

        var userDetails = User.builder()
                .username(username)
                .password(password)
                .authorities(authorities)
                .build();

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuth);
        System.out.println("[OAuth2] Spring Security context updated with new authentication.");

        // --- Conditional redirect based on 'state' parameter ---
        String state = request.getParameter("state");
        String accessToken = jwtProvider.generateAccessToken(newAuth);
        System.out.println("[OAuth2] JWT generated for user: " + username);

        if ("mobile".equals(state)) {
            // Mobile App Flow: Redirect with token in URL
            String targetUrl = UriComponentsBuilder.fromUriString("instiapp://callback")
                    .queryParam("token", accessToken)
                    .build().toUriString();

            System.out.println("[OAuth2] Mobile client detected. Redirecting to: " + targetUrl);
            response.sendRedirect(targetUrl);

        } else {
            // Web App Flow: Set HttpOnly cookies and return JSON payload
            String refreshToken = jwtProvider.generateRefreshToken(newAuth);

            Cookie accessCookie = new Cookie("accessToken", accessToken);
            accessCookie.setHttpOnly(true);
            accessCookie.setSecure(true);
            accessCookie.setPath("/");
            accessCookie.setMaxAge(3 * 24 * 60 * 60); // 3 days
            response.addCookie(accessCookie);

            Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(true);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(15 * 24 * 60 * 60); // 15 days
            response.addCookie(refreshCookie);

            System.out.println("[OAuth2] Web client detected. Returning JSON payload.");
            String message = isNew
                    ? "Auto-signup via Google successful"
                    : "Google login successful";
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(
                    String.format(
                            "{\"username\":\"%s\",\"message\":\"%s\",\"accessToken\":\"%s\",\"refreshToken\":\"%s\"}",
                            username, message, accessToken, refreshToken
                    )
            );
        }
    }
}
