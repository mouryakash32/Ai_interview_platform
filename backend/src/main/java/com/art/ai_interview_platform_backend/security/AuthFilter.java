package com.art.ai_interview_platform_backend.security;

import com.art.ai_interview_platform_backend.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailServiceImpl userDetailService;
    AuthFilter(JwtService jwtService, UserDetailServiceImpl userDetailService){
        this.jwtService = jwtService;
        this.userDetailService = userDetailService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        String email = jwtService.getEmail(token);

        if (email != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails user = userDetailService.loadUserByUsername(email);

            if (jwtService.isTokenValid(token, user)) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities()
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
