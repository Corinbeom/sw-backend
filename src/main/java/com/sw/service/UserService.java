package com.sw.service;

import com.sw.model.User;
import com.sw.model.dto.RegisterRequest;
import com.sw.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void registerUser(RegisterRequest request) {
        // 중복 확인 및 이메일 인증 코드 검증
        if (!isUserIdAvailable(request.getUserId())) {
            throw new IllegalArgumentException("User ID is already taken");
        }
        if (!isNickNameAvailable(request.getNickName())) {
            throw new IllegalArgumentException("Nickname is already taken");
        }
        if (!isUserEmailAvailable(request.getUserEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }
        if (!emailService.verifyCode(request.getUserEmail(), request.getVerificationCode())) {
            throw new IllegalArgumentException("Invalid email verification code");
        }

        // Register user
        User user = new User();
        user.setUserId(request.getUserId());
        user.setNickName(request.getNickName());
        user.setUserEmail(request.getUserEmail());

        //  비밀번호 암호화 후 저장
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        user.setVerified(true);

        userRepository.save(user);
    }

    @Transactional
    public void verifyUser(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));

        user.setVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);
    }

    //  아이디 중복 검사
    public boolean isUserIdAvailable(String userId) {
        return userRepository.findByUserId(userId).isEmpty();
    }

    //  닉네임 중복 검사
    public boolean isNickNameAvailable(String nickName) {
        return userRepository.findByNickName(nickName).isEmpty();
    }

    public boolean isUserEmailAvailable(String userEmail) {
        return userRepository.findByUserEmail(userEmail).isEmpty();
    }

}
