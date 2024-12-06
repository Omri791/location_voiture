package org.mdw31.tp4SOA;



import org.glassfish.jersey.server.ResourceConfig;
import org.mdw31.tp4SOA.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity  // Enables Spring Security
public class myconfig {

    // Register Jersey services (if necessary)
    @Bean
    public ResourceConfig resourceConfig() {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(AgenceRS.class);
        resourceConfig.register(ClientRS.class);
        resourceConfig.register(VoitureRS.class);
        resourceConfig.register(LocationRS.class);
        resourceConfig.register(UserService.class);
        return resourceConfig;
    }

    // Configure CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:8080")); // Allowed origins
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allowed methods
        corsConfiguration.setAllowCredentials(true); // Allow credentials (cookies)
        corsConfiguration.setAllowedHeaders(List.of("*")); // Allow all headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // Apply to all paths
        return source;
    }

    // Configure Spring Security for CORS support and CSRF handling
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()  // Allow all requests (adjust as needed)
                        .anyRequest().authenticated()  // Require authentication for other requests
                )
                .csrf(csrf -> csrf.disable())  // Disable CSRF for REST APIs (adjust if necessary)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));  // Use the custom CORS configuration

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt for password encoding
    }
}
