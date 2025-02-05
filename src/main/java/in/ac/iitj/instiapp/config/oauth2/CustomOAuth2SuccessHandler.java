package in.ac.iitj.instiapp.config.oauth2;

import in.ac.iitj.instiapp.Utils.AESUtil;
import in.ac.iitj.instiapp.services.JWTTokens.JWTService;
import in.ac.iitj.instiapp.services.JWTTokens.JWTTempTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final JWTTempTokenService jwtTempTokenService;

    public CustomOAuth2SuccessHandler(OAuth2AuthorizedClientService authorizedClientService, JWTTempTokenService jwtTempTokenService) {
        this.authorizedClientService = authorizedClientService;
        this.jwtTempTokenService = jwtTempTokenService;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if(authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken) {

            OAuth2AuthorizedClient oAuth2AuthorizedClient = authorizedClientService.loadAuthorizedClient(
                    oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(),
                    oAuth2AuthenticationToken.getName()
            );

            if(oAuth2AuthorizedClient != null) {

                OAuth2AccessToken accessToken = oAuth2AuthorizedClient.getAccessToken();
                OAuth2RefreshToken refreshToken = oAuth2AuthorizedClient.getRefreshToken();

                OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();
                String email = oAuth2User.getAttribute("email");
                String name = oAuth2User.getAttribute("name");
                String avatar = oAuth2User.getAttribute("picture");

                if(StringUtils.hasText(email) && StringUtils.hasText(name) && StringUtils.hasText(avatar)) {
                    Map<String, Object> additionalInformation = new HashMap<>();
                    additionalInformation.put("email", email);
                    additionalInformation.put("name", name);
                    additionalInformation.put("avatar", avatar);

                    String token =  jwtTempTokenService.GenerateToken(accessToken.getTokenValue(), refreshToken.getTokenValue(), AESUtil.decrypt(request.getParameter("state")), additionalInformation);

                    Cookie cookie = new Cookie("Authorization", String.format("Bearer %s", token));
                    cookie.setHttpOnly(true);
                    cookie.setSecure(true);
                    cookie.setPath("/");
                    cookie.setMaxAge(Math.toIntExact(JWTTempTokenService.EXPIRATION_TIME));
                    cookie.setAttribute("SameSite", "Strict");

                    response.addCookie(cookie);

                }else {
                    throw new BadCredentialsException("Invalid authentication credentials");
                }
            }
        }
    }
}
