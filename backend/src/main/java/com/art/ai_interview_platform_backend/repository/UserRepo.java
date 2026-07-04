package com.art.ai_interview_platform_backend.repository;

import com.art.ai_interview_platform_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User>findByEmail(String email);
}
