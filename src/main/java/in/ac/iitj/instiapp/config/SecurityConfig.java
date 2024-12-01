package in.ac.iitj.instiapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth ->{
                    auth.requestMatchers("/").permitAll();
                    auth.anyRequest().authenticated();
                        }
                )
                .oauth2Login(Customizer.withDefaults()).formLogin(Customizer.withDefaults());

        return http.build();
    }
}
