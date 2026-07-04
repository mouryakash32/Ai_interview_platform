package com.art.ai_interview_platform_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final UserDetailServiceImpl userDetailService;
    private final AuthFilter authFilter;
    public SecurityConfig(UserDetailServiceImpl userDetailService, AuthFilter authFilter){
        this.userDetailService = userDetailService;
        this.authFilter = authFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        return httpSecurity
                .formLogin(form->form.disable())
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth->auth.requestMatchers( "/auth/**").permitAll()
                        .requestMatchers("/user/**").hasAuthority("USER")
                        .requestMatchers("/admin/**").hasAuthority("ADMIN"))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    UserDetailsService userDetailsService(){
        return userDetailService;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider(userDetailsService());
        dao.setPasswordEncoder(passwordEncoder());
        return dao;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config){
        return config.getAuthenticationManager();
    }
}