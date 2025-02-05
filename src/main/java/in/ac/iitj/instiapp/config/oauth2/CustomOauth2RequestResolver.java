package in.ac.iitj.instiapp.config.oauth2;

import in.ac.iitj.instiapp.Utils.AESUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.server.ResponseStatusException;

public class CustomOauth2RequestResolver  implements OAuth2AuthorizationRequestResolver {


    private final DefaultOAuth2AuthorizationRequestResolver defaultOAuth2AuthorizationRequestResolver;


    public CustomOauth2RequestResolver(ClientRegistrationRepository clientRegistrationRepository, String baseUri) {
        this.defaultOAuth2AuthorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, baseUri);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest authorizationRequest = this.defaultOAuth2AuthorizationRequestResolver.resolve(request);

        return  customizeAuthorizationRequest(authorizationRequest, request);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest defaultRequest = this.defaultOAuth2AuthorizationRequestResolver.resolve(request, clientRegistrationId);
        return customizeAuthorizationRequest(defaultRequest, request);
    }



    private OAuth2AuthorizationRequest customizeAuthorizationRequest(OAuth2AuthorizationRequest defaultRequest, HttpServletRequest request) {
        if (defaultRequest == null) {
            return null;
        }

        String deviceId = request.getParameter("deviceId");

        if(deviceId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter 'deviceId'");
        }
        // Customize the OAuth2AuthorizationRequest as needed
        return OAuth2AuthorizationRequest.from(defaultRequest)
                .state(AESUtil.encrypt(deviceId)) // Example customization
                .build();
    }

}
