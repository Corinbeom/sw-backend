package com.sw.repository;

import com.sw.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserEmail(String userEmail);
    Optional<User> findByVerificationToken(String token);
    Optional<User> findByUserId(String userId);
    Optional<User> findByNickName(String nickName);
}
