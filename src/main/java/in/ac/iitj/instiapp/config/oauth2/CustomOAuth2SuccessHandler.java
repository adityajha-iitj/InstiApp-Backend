package in.ac.iitj.instiapp.config.oauth2;

import com.cloudinary.utils.StringUtils;
import in.ac.iitj.instiapp.Utils.AESUtil;
import in.ac.iitj.instiapp.Utils.CookieHelper;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import in.ac.iitj.instiapp.services.JWTTokens.JWEConstants;
import in.ac.iitj.instiapp.services.JWTTokens.JWEOAuth2Tokens;
import in.ac.iitj.instiapp.services.JWTTokens.TokenService;
import in.ac.iitj.instiapp.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final JWEOAuth2Tokens jweoAuth2Tokens;

    private final UserService userService;
    private final TokenService tokenService;

    public CustomOAuth2SuccessHandler(OAuth2AuthorizedClientService authorizedClientService, JWEOAuth2Tokens jweoAuth2Tokens, UserService userService, TokenService tokenService) {
        this.authorizedClientService = authorizedClientService;

        this.jweoAuth2Tokens = jweoAuth2Tokens;

        this.userService = userService;
        this.tokenService = tokenService;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;


        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(token.getAuthorizedClientRegistrationId(), token.getName());


        String accessToken = client.getAccessToken().getTokenValue();
        String refreshToken = client.getRefreshToken() != null ? client.getRefreshToken().getTokenValue() : null;

        String email = token.getPrincipal().getAttribute("email");
        Long id = userService.emailExists(email);
        if (id != -1L) {
            UserDetailedDto userDetailedDto = userService.getUserDetailed(email);

            tokenService.generateAndSaveRefreshTokenToken(response, new String[]{userDetailedDto.getUserName(), userDetailedDto.getEmail(), userDetailedDto.getName(), AESUtil.decrypt(request.getParameter("state")), userDetailedDto.getPhoneNumber(), userDetailedDto.getUserTypeName()}, id, Pair.of(accessToken, StringUtils.isBlank(refreshToken) ? "" : refreshToken));

        } else {

            try {
                Optional<String> jweToken = jweoAuth2Tokens.generateToken(accessToken, refreshToken, AESUtil.decrypt(request.getParameter("state")), token.getPrincipal());
                if (jweToken.isEmpty()) {
                    throw new BadCredentialsException("Invalid authentication credentials");
                }

                CookieHelper.setAuthCookie(response, jweToken.get(), JWEConstants.ExpirationDuration.SHORT, CookieHelper.HEADER_SAMESITE_NONE);
                
           }

            //DeviceId cannot be decrypted
            catch (IllegalArgumentException e){
                response.sendRedirect("/error");
            }

        }

    }

}
