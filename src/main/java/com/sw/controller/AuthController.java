package com.sw.controller;

import com.sw.model.User;
import com.sw.model.dto.RegisterRequest;
import com.sw.service.EmailService;
import com.sw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    private EmailService emailService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String token) {
        userService.verifyUser(token);
        return ResponseEntity.ok("Email verified successfully!");
    }

    //  아이디 중복 검사
    @GetMapping("/check-id")
    public ResponseEntity<Boolean> checkUserId(@RequestParam String userId) {
        boolean isAvailable = userService.isUserIdAvailable(userId);
        return ResponseEntity.ok(isAvailable);
    }

    //  닉네임 중복 검사
    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickName(@RequestParam String nickName) {
        boolean isAvailable = userService.isNickNameAvailable(nickName);
        return ResponseEntity.ok(isAvailable);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String userEmail) {
        boolean isAvailable = userService.isUserEmailAvailable(userEmail);
        return ResponseEntity.ok(isAvailable);
    }

    //  이메일로 인증번호 전송
    @PostMapping("/send-email")
    public ResponseEntity<String> sendVerificationEmail(@RequestParam String userEmail) {
        String verificationCode = emailService.sendVerificationCode(userEmail);
        return ResponseEntity.ok("Verification code sent to " + userEmail);
    }

    //  인증번호 검증
    @PostMapping("/verify-email")
    public ResponseEntity<Boolean> verifyEmailCode(@RequestParam String userEmail, @RequestParam String code) {
        boolean isVerified = emailService.verifyCode(userEmail, code);
        if (isVerified) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }
}
