package com.music.config;

import com.music.model.User;
import com.music.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.util.Optional;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> {
            Optional<User> userOptional = userRepo.findByUsername(username);
            if (userOptional.isPresent()) {
                return userOptional.get();
            } else {
                throw new UsernameNotFoundException("User '" + username + "' not found");
            }
        };
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/dashboard").hasAnyRole("ADMIN", "ARTIST")
                        .requestMatchers("/dashboard/new-artist").hasRole("ADMIN")
                        .requestMatchers("/dashboard/new-track").hasRole("ARTIST")
                        .requestMatchers("/dashboard/new-album").hasRole("ARTIST")
                        .requestMatchers("/dashboard/album/**").hasRole("ARTIST")
                        .requestMatchers("/fav/**").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(loginConfigurer -> loginConfigurer
                        .loginPage("/") // Redirect to home page if login is required
                        .loginProcessingUrl("/auth/login") // Custom login endpoint
                        .defaultSuccessUrl("/?login=true") // Redirect after successful login
                        .failureUrl("/?error=true") // Redirect after failed login
                        .permitAll()
                )
                .logout(logoutConfigurer -> logoutConfigurer
                        .logoutUrl("/auth/logout") // Custom logout endpoint
                        .logoutSuccessUrl("/?logout=true") // Redirect after logout
                        .invalidateHttpSession(true) // Invalidate session
                        .permitAll()
                ).exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(accessDeniedHandler()) // Handle access denied
                        .authenticationEntryPoint(authenticationEntryPoint()) // Handle unauthorized access
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (HttpServletRequest request, HttpServletResponse response, org.springframework.security.access.AccessDeniedException accessDeniedException) -> {
            // Redirect to a custom "403 Access Denied" page
            response.sendRedirect("/dashboard");
        };
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) -> {
            // Redirect to the login page for unauthorized access
            response.sendRedirect("/");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
