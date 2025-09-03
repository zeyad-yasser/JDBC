package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.config;


import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.filter.ValidationHeaderFilter;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final ValidationHeaderFilter validationHeaderFilter;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          ValidationHeaderFilter validationHeaderFilter) {
        this.userDetailsService = userDetailsService;
        this.validationHeaderFilter = validationHeaderFilter;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Allow all GET requests without authentication
                        .requestMatchers(HttpMethod.GET).permitAll()
                        // Allow specific auth endpoints (POST)
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        // Allow API documentation endpoints
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        // All other requests (POST, PUT, DELETE, etc.) require authentication
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(validationHeaderFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // I need to tell my AuthenticationManager to Authenticate the User by CustomUserDetailsService
    @Bean
    public AuthenticationManager authManager(HttpSecurity http,PasswordEncoder passwordEncoder) throws Exception {

        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder); // Use BCrypt here
        return builder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
