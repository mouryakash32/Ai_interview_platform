package com.art.ai_interview_platform_backend.utils;

import com.art.ai_interview_platform_backend.entity.User;
import com.art.ai_interview_platform_backend.repository.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class GetLoggedInUser {
    private final UserRepo userRepo;

    public GetLoggedInUser(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email =  authentication.getName();
        return userRepo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found Exception"));
    }
}
