package in.ac.iitj.instiapp.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

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
}
