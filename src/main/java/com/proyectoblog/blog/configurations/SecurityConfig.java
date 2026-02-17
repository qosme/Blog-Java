package com.proyectoblog.blog.configurations;

import com.proyectoblog.blog.domain.entities.User;
import com.proyectoblog.blog.repositories.UserRepository;
import com.proyectoblog.blog.security.BlogUserDetails;
import com.proyectoblog.blog.security.BlogUserDetailsService;
import com.proyectoblog.blog.security.JwtAuthFilter;
import com.proyectoblog.blog.services.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    //para spring security asegurando los endpoints

    @Bean
    public JwtAuthFilter jwtAuthFilter(AuthenticationService authenticationService) {
        return new JwtAuthFilter(authenticationService);
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        BlogUserDetailsService blogUserDetailsService = new BlogUserDetailsService(userRepository);

        String email = "usuario@test.com";
        userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder().name("Usuario Test").email(email).password(passwordEncoder().encode("password")).build();
            return userRepository.save(newUser);
        });
        return blogUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll() //se permiten los gets a la ubicación
                .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/tags/**").permitAll()
                .anyRequest().authenticated() //lo demás requiere autentificación
        ).csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    //para las contraseñas, con default en bcrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

