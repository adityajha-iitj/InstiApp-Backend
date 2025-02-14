package in.ac.iitj.instiapp.config.oauth2;

import com.nimbusds.jwt.JWTClaimsSet;
import in.ac.iitj.instiapp.Utils.CookieHelper;
import in.ac.iitj.instiapp.services.JWTTokens.JWEAuthenticationToken;
import in.ac.iitj.instiapp.services.JWTTokens.JWEConstants;
import in.ac.iitj.instiapp.services.JWTTokens.JWEOauth2Redirection;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import java.util.Optional;

public record CookieOAuth2AuthorizationRequestRepository(
        JWEOauth2Redirection jweOauth2Redirection) implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {


    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {

        if (SecurityContextHolder.getContext().getAuthentication() instanceof JWEAuthenticationToken jweAuthenticationToken) {

            if (jweAuthenticationToken.getState().equals(JWEConstants.STATES.STATE_UNREGISTERED)) {
                return jweOauth2Redirection.getPayload((JWTClaimsSet) jweAuthenticationToken.getCredentials()).orElse(null);
            }
            return null;
        }

        return null;
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        Optional<String> token = jweOauth2Redirection.generateToken(authorizationRequest);
        System.out.println(token);
        token.ifPresent(s -> CookieHelper.setAuthCookie(response, s, JWEConstants.ExpirationDuration.VERY_SHORT, CookieHelper.HEADER_SAMESITE_NONE));
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        // Retrieve the OAuth2AuthorizationRequest from the cookie
        OAuth2AuthorizationRequest authRequest = loadAuthorizationRequest(request);
        // Remove the cookie storing the authorization request
        CookieHelper.deleteAuthCookie(response);

        return authRequest;
    }

}
