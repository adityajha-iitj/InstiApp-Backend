package in.ac.iitj.instiapp.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.SessionTrackingMode;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.sql.DataSource;
import java.security.SecureRandom;
import java.util.Set;

@Component
public class IndependentBeans {

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return  new JdbcTemplate(dataSource);
    }

    @Bean
    public Dotenv getDotEnv(){
        return  Dotenv.load();
    }

    @Bean
    public SecureRandom getSecureRandom(){
        return new SecureRandom();
    }

    @Bean
    public AntPathMatcher getAntPathMatcher(){
        return new AntPathMatcher();
    }

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return servletContext -> {
            servletContext.setSessionTrackingModes(Set.of(SessionTrackingMode.COOKIE));
        };
    }
}
