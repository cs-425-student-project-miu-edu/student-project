package mscs.hms.config;

import mscs.hms.services.IUserService;
import mscs.hms.services.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class BeansConfig {

    /*
    @Bean
    protected IUserService getUserService() {
        return new UserServiceImpl();
    }
    */

    @Bean
    protected BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}