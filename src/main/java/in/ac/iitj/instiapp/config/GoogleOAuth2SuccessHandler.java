package in.ac.iitj.instiapp.config;

import in.ac.iitj.instiapp.database.entities.User.Usertype;
import in.ac.iitj.instiapp.payload.Auth.SignupDto;
import in.ac.iitj.instiapp.payload.User.UserBaseDto;
import in.ac.iitj.instiapp.services.UserService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, java.io.IOException {
        System.out.println("[OAuth2] Authentication successful. Extracting OAuth2 user...");

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

        UserBaseDto userBaseDto = userService.getUserLimited(username);
        System.out.println("[OAuth2] Retrieved UserBaseDto for login.");

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(userBaseDto.getUserTypeName())
        );

        var userDetails = User.builder()
                .username(username)
                .password(userBaseDto.getPassword())
                .authorities(authorities)
                .build();

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuth);
        System.out.println("[OAuth2] Spring Security context updated with new authentication.");

        String accessToken = jwtProvider.generateAccessToken(newAuth);
        String refreshToken = jwtProvider.generateRefreshToken(newAuth);
        System.out.println("[OAuth2] Tokens generated successfully.");

        String message = isNew ? "Auto-signup via Google successful" : "Google login successful";
        String jsonResponse = String.format(
                "{\"accessToken\":\"%s\", \"refreshToken\":\"%s\", \"username\":\"%s\", \"message\":\"%s\"}",
                accessToken, refreshToken, username, message
        );

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
        System.out.println("[OAuth2] JSON response written to output stream.");
    }

}

