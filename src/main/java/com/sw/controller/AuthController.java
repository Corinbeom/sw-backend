package com.sw.controller;

import com.sw.config.JwtUtils;
import com.sw.model.User;
import com.sw.model.dto.*;
import com.sw.repository.UserRepository;
import com.sw.service.EmailService;
import com.sw.service.UserService;
import io.jsonwebtoken.Jwt;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {



    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private final JwtUtils jwtUtils;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EmailService emailService;

    @Autowired
    public AuthController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    //  회원가입
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        logger.info("회원가입 요청: 이메일={}, 닉네임={}", request.getUserEmail(), request.getNickName());
        logger.info("평문 비밀번호: {}", request.getPassword());

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("비밀번호가 비어 있습니다.");
        }

        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        logger.info("암호화된 비밀번호: {}", hashedPassword);

        User user = new User();
        user.setUserEmail(request.getUserEmail());
        user.setNickName(request.getNickName());
        user.setPassword(hashedPassword);
        userRepository.save(user);

        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    //  검증
    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String token) {
        userService.verifyUser(token);
        return ResponseEntity.ok("Email verified successfully!");
    }

    //  닉네임 중복 검사
    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNickName(@RequestParam String nickName) {
        boolean isAvailable = userService.isNickNameAvailable(nickName);
        return ResponseEntity.ok(isAvailable);
    }

    //  이메일 중복 검사
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String userEmail) {
        boolean isAvailable = userService.isUserEmailAvailable(userEmail);
        return ResponseEntity.ok(isAvailable);
    }

    //  이메일로 인증번호 전송
    @PostMapping("/send-email")
    public ResponseEntity<?> sendVerificationEmail(@RequestBody EmailRequest emailRequest) {
        String userEmail = emailRequest.getUserEmail();
        String verificationCode = emailService.sendVerificationCode(userEmail);
        System.out.println("인증번호 : " + verificationCode);
        return ResponseEntity.ok("Verification code sent to " + userEmail);
    }

    //  인증번호 검증
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmailCode(@RequestBody EmailVerificationRequest request) {
        boolean isValid = emailService.verifyCode(request.getUserEmail(), request.getCode());
        return ResponseEntity.ok(isValid);
    }
}
