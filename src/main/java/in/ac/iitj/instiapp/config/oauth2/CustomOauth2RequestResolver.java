package in.ac.iitj.instiapp.config.oauth2;

import in.ac.iitj.instiapp.Utils.AESUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomOauth2RequestResolver  implements OAuth2AuthorizationRequestResolver {


    private static final Logger log = LoggerFactory.getLogger(CustomOauth2RequestResolver.class);
    private final DefaultOAuth2AuthorizationRequestResolver defaultOAuth2AuthorizationRequestResolver;


    public CustomOauth2RequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        this.defaultOAuth2AuthorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {


        OAuth2AuthorizationRequest authorizationRequest = this.resolve(request,"google");

        return  customizeAuthorizationRequest(authorizationRequest, request);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        if(authentication != null){
            return null;
        }

        OAuth2AuthorizationRequest defaultRequest = this.defaultOAuth2AuthorizationRequestResolver.resolve(request, clientRegistrationId);

        return customizeAuthorizationRequest(defaultRequest, request);
    }



    private OAuth2AuthorizationRequest customizeAuthorizationRequest(OAuth2AuthorizationRequest defaultRequest, HttpServletRequest request) {
        if (defaultRequest == null) {
            return null;
        }

        String deviceId = request.getParameter("deviceId");

        if(deviceId == null) {
            return  defaultRequest;
       }
        // Customize the OAuth2AuthorizationRequest as needed
        return OAuth2AuthorizationRequest.from(defaultRequest)
                .state(AESUtil.encrypt(deviceId)) // Example customization
                .build();
    }

}
