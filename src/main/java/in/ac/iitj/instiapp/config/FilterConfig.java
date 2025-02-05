package in.ac.iitj.instiapp.config;

import in.ac.iitj.instiapp.authfilters.JWTTempTokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JWTTempTokenFilter> filterRegistrationBean(JWTTempTokenFilter jwtTempTokenFilter) {

        FilterRegistrationBean<JWTTempTokenFilter> filterRegistrationBean = new FilterRegistrationBean<>(jwtTempTokenFilter);
        filterRegistrationBean.addUrlPatterns("api/v1/auth/*");
        return filterRegistrationBean;

    }


}
