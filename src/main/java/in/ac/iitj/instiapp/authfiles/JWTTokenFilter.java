package in.ac.iitj.instiapp.authfiles;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import in.ac.iitj.instiapp.Utils.CookieHelper;
import in.ac.iitj.instiapp.services.JWTTokens.JWEAuthenticationToken;
import in.ac.iitj.instiapp.services.JWTTokens.JWEConstants;
import in.ac.iitj.instiapp.services.JWTTokens.JWEService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

@Component
public class JWTTokenFilter extends OncePerRequestFilter {

    private final JWEService jweService;

    @Autowired
    public JWTTokenFilter(JWEService jweService) {
        this.jweService = jweService;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        Optional<String> cookie = CookieHelper.getAuthCookieValue(request);
        System.out.println("Cookie234" + cookie);
        if (cookie.isPresent()) {
            try {
                JWTClaimsSet claimsSet = jweService.extractClaims(cookie.get());

                if (jweService.isExpired(claimsSet) && JWEConstants.STATES.STATE_APPROVED.toString().equals(claimsSet.getClaim(JWEConstants.KEYS_STATE))) {
                    response.sendRedirect("/api/v1/auth/refresh-token");
                    return;
                } else if (jweService.isExpired(claimsSet)) {
                    CookieHelper.deleteAuthCookie(response);
                    throw new BadCredentialsException("JWE Expired");
                }

                SecurityContextHolder.getContext().setAuthentication(new JWEAuthenticationToken(JWEConstants.STATES.valueOf((String) claimsSet.getClaim(JWEConstants.KEYS_STATE)), claimsSet.getSubject(), claimsSet));

            } catch (ParseException | JOSEException e) {
                CookieHelper.deleteAuthCookie(response);
                CookieHelper.deleteRefreshTokenCookie(response);

            }


        }


        filterChain.doFilter(request, response);
    }


}
