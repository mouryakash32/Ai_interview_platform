package com.art.ai_interview_platform_backend.security;

import com.art.ai_interview_platform_backend.entity.User;
import com.art.ai_interview_platform_backend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;
    public UserDetailServiceImpl(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            User user = userRepo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found Exception"));
                return new CustomeUserDetails(user);
    }
}
