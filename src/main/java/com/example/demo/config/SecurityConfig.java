package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/", "/register", "/login", "/logout", "/contact").permitAll()
                                .requestMatchers("/api/v1/student").permitAll()
                                .requestMatchers("/api/v1/subject").permitAll()
                                .requestMatchers("/api/v1/student/**").permitAll()
                                .requestMatchers("/api/v1/subject/**").permitAll()
                                .requestMatchers("/api/v1/addSubject").permitAll()
                                // .requestMatchers("/api/v1/addStudent").permitAll()
                                .anyRequest().permitAll()
                ).csrf(csrf -> csrf.disable())
                .formLogin(form -> form
                                .defaultSuccessUrl("/", true)
                )
                .logout(config -> config
                                .logoutSuccessUrl("/")
                )
               
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
