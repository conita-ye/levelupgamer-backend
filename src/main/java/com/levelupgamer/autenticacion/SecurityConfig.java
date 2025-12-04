package com.levelupgamer.autenticacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return this.authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests((authz) -> {
                    authz
                            .requestMatchers(HttpMethod.GET, "/").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/v1/auth/refresh").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/v1/users/register").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/products", "/api/v1/products/", "/api/v1/products/*").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/products/featured").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/products/*/reviews").permitAll()
                            .requestMatchers("/api/v1/blog-posts/**", "/api/v1/contact-messages/**").permitAll()
                            .requestMatchers("/api/v1/regions/**").permitAll()
                            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                            .requestMatchers("/actuator/health").permitAll()
                            .requestMatchers("/uploads/**").permitAll()
                            
                            .requestMatchers("/api/v1/users/roles").hasRole("ADMINISTRADOR")
                            .requestMatchers(HttpMethod.GET, "/api/v1/users").hasRole("ADMINISTRADOR")
                            .requestMatchers(HttpMethod.GET, "/api/v1/users/{id}").authenticated()
                            .requestMatchers(HttpMethod.PUT, "/api/v1/users/{id}").authenticated()

                            .requestMatchers(HttpMethod.POST, "/api/v1/products/**").hasAnyRole("ADMINISTRADOR", "VENDEDOR")
                            .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasAnyRole("ADMINISTRADOR", "VENDEDOR")
                            .requestMatchers(HttpMethod.PATCH, "/api/v1/products/**").hasAnyRole("ADMINISTRADOR", "VENDEDOR")
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasAnyRole("ADMINISTRADOR", "VENDEDOR")

                            .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").hasAnyRole("ADMINISTRADOR", "CLIENTE", "VENDEDOR")
                            .requestMatchers("/api/v1/categories/**").hasRole("ADMINISTRADOR")

                            .requestMatchers(HttpMethod.GET, "/api/v1/boletas/**").hasAnyRole("ADMINISTRADOR", "CLIENTE", "VENDEDOR")
                            .requestMatchers("/api/v1/boletas/**").hasAnyRole("ADMINISTRADOR", "CLIENTE")
                            .requestMatchers("/api/v1/cart/**").authenticated()
                            .requestMatchers("/api/v1/points/**").authenticated()
                            .requestMatchers(HttpMethod.POST, "/api/v1/reviews").hasAnyRole("CLIENTE", "ADMINISTRADOR")
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/reviews/**").hasAnyRole("CLIENTE", "ADMINISTRADOR")

                            .anyRequest().authenticated();
                })
                .addFilter(new JwtAutenticacionFilter(authenticationManager()))
                .csrf(config -> config.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(management ->
                        management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
