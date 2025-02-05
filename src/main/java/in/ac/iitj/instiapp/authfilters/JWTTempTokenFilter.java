package in.ac.iitj.instiapp.authfilters;

import in.ac.iitj.instiapp.services.JWTTokens.JWTService;
import in.ac.iitj.instiapp.services.JWTTokens.JWTTempTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@Component
public class JWTTempTokenFilter extends OncePerRequestFilter {

    private  final  JWTTempTokenService jwtTempTokenService;

    @Autowired
    public JWTTempTokenFilter(JWTTempTokenService jwtTempTokenService) {
        this.jwtTempTokenService = jwtTempTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Optional<String> token = extractToken(request.getCookies());

        if(!token.isPresent()) {
            throw new BadCredentialsException("Authorization Token not found");
        }
        if(!jwtTempTokenService.validateToken(token.get())) {
            throw new CredentialsExpiredException("Expired JWT token");
        }
        if(jwtTempTokenService.getTokenState(token.get()).equals(JWTService.STATES.STATE_APPROVED)){
            throw new AccessDeniedException("Access Denied");
        }

        PreAuthenticatedAuthenticationToken preAuthenticatedAuthenticationToken = new PreAuthenticatedAuthenticationToken(
                jwtTempTokenService.getPayload(token.get()),
                token,
                Collections.emptyList()
        );

        SecurityContextHolder.getContext().setAuthentication(preAuthenticatedAuthenticationToken);

        filterChain.doFilter(request, response);
    }


    private Optional<String> extractToken(Cookie[] cookies) {
        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> "Authorization".equals(cookie.getName()) && cookie.getValue().startsWith("Bearer"))
                .map(cookie -> cookie.getValue().substring(6))
                .findFirst();
    }
}
