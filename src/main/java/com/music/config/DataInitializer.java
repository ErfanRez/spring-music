package com.music.config;

import com.music.model.User;
import com.music.repository.UserRepository;
import com.music.utils.Roles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class DataInitializer {

    @Bean
    CommandLineRunner commandLineRunner(
            UserRepository userRepository, PasswordEncoder passwordEncoder
    ) {
        return args -> {
            User admin = userRepository.findByUsername("admin")
                    .orElse(new User("admin", passwordEncoder.encode("admin"), Roles.ADMIN, "admin@example.com"));
            userRepository.save(admin);

            User testUser = userRepository.findByUsername("test")
                    .orElse(new User("test", passwordEncoder.encode("test"), Roles.USER, "test@example.com"));
            userRepository.save(testUser);

            log.info("User data initialized");
        };
    }
}
