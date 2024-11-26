package com.sw.service;

import com.sw.config.JwtUtils;
import com.sw.model.User;
import com.sw.model.dto.RegisterRequest;
import com.sw.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(String.class);

    private final UserRepository userRepository;
    private final EmailService emailService;
    private JwtUtils jwtUtils;

    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public User login(String email, String rawPassword) {
        // 이메일로 사용자 조회
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));


        if (!BCrypt.checkpw(rawPassword, user.getPassword())) {
            System.out.println("비밀번호 검증 실패!");
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        System.out.println("비밀번호 검증 성공!");

        return user;
    }

    @Transactional
    public void verifyUser(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));

        user.setVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);
    }

    //  닉네임 중복 검사
    public boolean isNickNameAvailable(String nickName) {
        return userRepository.findByNickName(nickName).isEmpty();
    }

    public boolean isUserEmailAvailable(String userEmail) {
        return userRepository.findByUserEmail(userEmail).isEmpty();
    }

    public User findByUserEmail(String userEmail) {
        Optional<User> userInfo = userRepository.findByUserEmail(userEmail);
        return userInfo.orElse(null);
    }

    public User setTestUser(User user) {
        user.setRole("USER");
        return userRepository.save(user);
    }

}
