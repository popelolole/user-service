package se.kthraven.userservice.config;

import org.aspectj.bridge.IMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import se.kthraven.userservice.Model.*;
import se.kthraven.userservice.Persistence.*;

@Configuration
public class AppConfig {

    @Bean
    public IUserPersistence IUserPersistence(){
        return new UserPersistence();
    }
}
