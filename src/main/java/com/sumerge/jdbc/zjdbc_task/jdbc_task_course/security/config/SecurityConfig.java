package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.security.config;


import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.security.filter.ValidationHeaderFilter;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.security.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    private final ValidationHeaderFilter validationHeaderFilter;


    public SecurityConfig(CustomUserDetailsService userDetailsService, ValidationHeaderFilter validationHeaderFilter)
    {
        this.userDetailsService = userDetailsService;
        this.validationHeaderFilter = validationHeaderFilter;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->auth
                        .requestMatchers(HttpMethod.GET, "/courses/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/courses").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/courses/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/courses/**").authenticated()
                        .anyRequest().denyAll()
                )
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(validationHeaderFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception
    {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
        return builder.build();

    }


}
