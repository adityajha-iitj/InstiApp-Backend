package in.ac.iitj.instiapp.config;

import in.ac.iitj.instiapp.authfiles.JWTTokenFilter;
import in.ac.iitj.instiapp.config.oauth2.CookieOAuth2AuthorizationRequestRepository;
import in.ac.iitj.instiapp.config.oauth2.CustomOAuth2SuccessHandler;
import in.ac.iitj.instiapp.config.oauth2.CustomOauth2RequestResolver;
import in.ac.iitj.instiapp.services.JWTTokens.JWEOauth2Redirection;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.NullSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {


    private final  JWTTokenFilter jwtTokenFilter;
    private final JWEOauth2Redirection jweOauth2Redirection;

    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final CustomOauth2RequestResolver customOauth2RequestResolver;

    private static final List<String> PERMIT_ALL_PATHS = List.of(
          "/oauth2/authorization/**", "/login/oauth2/code/**","/error/**","/api/v1/auth/logout","/api/v1/auth/status"
    );




    @Autowired
    public SecurityConfig(JWTTokenFilter jwtTokenFilter, JWEOauth2Redirection jweOauth2Redirection, CustomOAuth2SuccessHandler customOAuth2SuccessHandler, CustomOauth2RequestResolver customOauth2RequestResolver) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.jweOauth2Redirection = jweOauth2Redirection;

        this.customOAuth2SuccessHandler = customOAuth2SuccessHandler;
        this.customOauth2RequestResolver = customOauth2RequestResolver;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http ) throws Exception {


        http.authorizeHttpRequests(auth2 -> auth2
                .requestMatchers(PERMIT_ALL_PATHS.toArray(new String[0]))
                .permitAll()
                        .anyRequest().authenticated()
        ).cors(Customizer.withDefaults())
                .addFilterBefore(jwtTokenFilter, OAuth2AuthorizationRequestRedirectFilter.class )
                .oauth2Login(oauth2 -> oauth2

                        .authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig
                                .authorizationRequestRepository(new CookieOAuth2AuthorizationRequestRepository(jweOauth2Redirection))
                                .authorizationRequestResolver(customOauth2RequestResolver)

                        )

                        .successHandler(customOAuth2SuccessHandler)

                )



                // Doing to prevent creation of jsession id.Request caching could be done through CookieOAuth2AuthorizationRequestRepository

                .securityContext(ctx -> ctx.securityContextRepository(new NullSecurityContextRepository()))

                .cors(Customizer.withDefaults())

                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .defaultAuthenticationEntryPointFor(unauthorizedEntryPoint(),new AntPathRequestMatcher("/**"))
                )
                .csrf(AbstractHttpConfigurer::disable)

        ;
        return http.build();
    }

    private AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> {
            if (request.getRequestURI().equals("/login")) {
                response.sendRedirect("/oauth2/authorization/google"); // Redirect to OAuth login
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"); // Return 401 for others
            }
        };



    }

    public static List<String> getPermitAllPaths(){
        return PERMIT_ALL_PATHS;
    }









}
