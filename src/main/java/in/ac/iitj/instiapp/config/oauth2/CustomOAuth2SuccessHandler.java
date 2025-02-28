package in.ac.iitj.instiapp.config.oauth2;

import com.cloudinary.utils.StringUtils;
import in.ac.iitj.instiapp.Utils.AESUtil;
import in.ac.iitj.instiapp.Utils.CookieHelper;
import in.ac.iitj.instiapp.payload.User.UserDetailedDto;
import in.ac.iitj.instiapp.services.JWTTokens.JWEConstants;
import in.ac.iitj.instiapp.services.JWTTokens.JWEOAuth2Tokens;
import in.ac.iitj.instiapp.services.JWTTokens.TokenService;
import in.ac.iitj.instiapp.services.UserService;
import in.ac.iitj.instiapp.services.UtilitiesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final JWEOAuth2Tokens jweoAuth2Tokens;

    private final UserService userService;
    private final TokenService tokenService;
    private final UtilitiesService utilitiesService;


    @Value("${user.base.url}")
    public String userBaseUrl;


    public CustomOAuth2SuccessHandler(OAuth2AuthorizedClientService authorizedClientService, JWEOAuth2Tokens jweoAuth2Tokens, UserService userService, TokenService tokenService, UtilitiesService utilitiesService) {
        this.authorizedClientService = authorizedClientService;

        this.jweoAuth2Tokens = jweoAuth2Tokens;

        this.userService = userService;
        this.tokenService = tokenService;
        this.utilitiesService = utilitiesService;

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
            utilitiesService.writeToResponse(response, JWEConstants.STATES.STATE_APPROVED.name(), HttpStatus.OK);
            getRedirectStrategy().sendRedirect(request,response,userBaseUrl);

        } else {

            try {
                Optional<String> jweToken = jweoAuth2Tokens.generateToken(accessToken, refreshToken, AESUtil.decrypt(request.getParameter("state")), token.getPrincipal());
                if (jweToken.isEmpty()) {
                    throw new BadCredentialsException("Invalid authentication credentials");
                }

                CookieHelper.setAuthCookie(response, jweToken.get(), JWEConstants.ExpirationDuration.SHORT, CookieHelper.HEADER_SAMESITE_LAX);
                utilitiesService.writeToResponse(response, JWEConstants.STATES.STATE_PENDING.name(), HttpStatus.OK);
                getRedirectStrategy().sendRedirect(request, response, URI.create(userBaseUrl).resolve("signup").toString());
           }

            //DeviceId cannot be decrypted
            catch (IllegalArgumentException e){
                response.sendRedirect("/error");
            }

        }


        /*
        * This line has a great role don't change it until you know what you are doing.
        * What the app's security config is that it doesn't uses spring internal session mechanism
        * But when oauth2 client makes request to google's oauth2 server it creates a session
        * So the same session id is used by the app.To remove that and not to pass the user which could break the ouath2
        * mechanism we remove the cookie.
        * */
        CookieHelper.deleteJSessionIdCookie(response);
    }

}
