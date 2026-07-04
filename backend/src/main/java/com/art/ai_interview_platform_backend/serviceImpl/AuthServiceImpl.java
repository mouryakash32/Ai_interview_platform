package com.art.ai_interview_platform_backend.serviceImpl;

import com.art.ai_interview_platform_backend.dto.auth.LoginReq;
import com.art.ai_interview_platform_backend.dto.auth.RegisterReq;
import com.art.ai_interview_platform_backend.dto.auth.AuthRes;
import com.art.ai_interview_platform_backend.entity.Roles;
import com.art.ai_interview_platform_backend.entity.User;
import com.art.ai_interview_platform_backend.repository.UserRepo;
import com.art.ai_interview_platform_backend.security.CustomeUserDetails;
import com.art.ai_interview_platform_backend.security.JwtService;
import com.art.ai_interview_platform_backend.security.SecurityConfig;
import com.art.ai_interview_platform_backend.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    public AuthServiceImpl(UserRepo userRepo, AuthenticationManager authenticationManager, JwtService jwtService, PasswordEncoder passwordEncoder){
        this.userRepo =userRepo;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public AuthRes register(RegisterReq registerReq) {
        try{
            Optional<User> user2 =  userRepo.findByEmail(registerReq.getEmail());
            if(user2.isPresent()){
                throw new RuntimeException("Email is not unique");
            }
            User user = new User();
            user.setEmail(registerReq.getEmail());
            user.setName(registerReq.getName());
            user.setPassword(passwordEncoder.encode(registerReq.getPassword()));
            user.setRoles(Set.of(Roles.USER));
            userRepo.save(user);
            AuthRes authRes = new AuthRes();
            authRes.setMessage("Registeration successfull");
            authRes.setStatus(201);
            return authRes;
        }catch (Exception e){
            AuthRes authRes = new AuthRes();
            authRes.setMessage("Exception occur at registeration : "+ e);
            authRes.setStatus(400);
            return authRes;
        }

    }

    @Override
    public AuthRes login(LoginReq loginReq) {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));
            System.out.println("Error while authenticating");
            UserDetails userDetails =(UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);
            AuthRes authRes = new AuthRes();
            authRes.setStatus(200);
            authRes.setMessage(token);
            return authRes;
        }catch (Exception e){
            AuthRes authRes = new AuthRes();
            authRes.setStatus(401);
            authRes.setMessage("Exception occur at the login level");
            return authRes;
        }
    }
}
