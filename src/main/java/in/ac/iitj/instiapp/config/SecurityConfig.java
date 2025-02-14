package in.ac.iitj.instiapp.config;

import in.ac.iitj.instiapp.authfiles.JWTTokenFilter;
import in.ac.iitj.instiapp.config.oauth2.CookieOAuth2AuthorizationRequestRepository;
import in.ac.iitj.instiapp.config.oauth2.CustomOAuth2SuccessHandler;
import in.ac.iitj.instiapp.config.oauth2.CustomOauth2RequestResolver;
import in.ac.iitj.instiapp.services.JWTTokens.JWEOAuth2Tokens;
import in.ac.iitj.instiapp.services.JWTTokens.JWEOauth2Redirection;
import in.ac.iitj.instiapp.services.JWTTokens.TokenService;
import in.ac.iitj.instiapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.context.NullSecurityContextRepository;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {


    private final  JWTTokenFilter jwtTokenFilter;
    private final JWEOauth2Redirection jweOauth2Redirection;
    private final JWEOAuth2Tokens jweoAuth2Tokens;
    private final TokenService tokenService;
    private final UserService userService;

    @Autowired
    public SecurityConfig(JWTTokenFilter jwtTokenFilter, JWEOauth2Redirection jweOauth2Redirection, JWEOAuth2Tokens jweoAuth2Tokens, TokenService tokenService, UserService userService) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.jweOauth2Redirection = jweOauth2Redirection;
        this.jweoAuth2Tokens = jweoAuth2Tokens;
        this.tokenService = tokenService;
        this.userService = userService;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientService authorizedClientService ) throws Exception {
        CustomOauth2RequestResolver customOauth2RequestResolver = new CustomOauth2RequestResolver(clientRegistrationRepository);


        http.authorizeHttpRequests(auth2 -> auth2
                .requestMatchers("/oauth2/authorization/**", "/login/oauth2/code/**","/authorize/oauth2/**","/error/**","/logout","/api/v1/auth/status")
                .permitAll()
                        .anyRequest().authenticated()
        ).addFilterBefore(jwtTokenFilter, OAuth2AuthorizationRequestRedirectFilter.class )
                .oauth2Login(oauth2 -> oauth2
                        .loginProcessingUrl("/login/oauth2/**")

                        .authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig
                                .authorizationRequestRepository(new CookieOAuth2AuthorizationRequestRepository(jweOauth2Redirection))
                                .authorizationRequestResolver(customOauth2RequestResolver)

                        )

                        .successHandler(new CustomOAuth2SuccessHandler(authorizedClientService, jweoAuth2Tokens ,userService,tokenService))

                )

                // Doing to prevent creation of jsession id.Request caching could be done through CookieOAuth2AuthorizationRequestRepository

                .securityContext(ctx -> ctx.securityContextRepository(new NullSecurityContextRepository()))



                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.FORBIDDEN))
                )
                .csrf(AbstractHttpConfigurer::disable)

        ;
        return http.build();
    }







}
