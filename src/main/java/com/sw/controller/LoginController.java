package com.sw.controller;

import com.sw.config.JwtUtils;
import com.sw.model.User;
import com.sw.model.dto.LoginResponse;
import com.sw.repository.UserRepository;
import com.sw.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody User request) {
        logger.info("로그인 요청: 이메일={}, 비밀번호={}", request.getUserEmail(), request.getPassword());

        User user = userService.findByUserEmail(request.getUserEmail());
        if (user == null) {
            logger.error("사용자를 찾을 수 없습니다. 이메일: {}", request.getUserEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        logger.info("저장된 비밀번호: {}", user.getPassword());
        boolean passwordMatch = BCrypt.checkpw(request.getPassword(), user.getPassword());
        logger.info("비밀번호 비교 결과: {}", passwordMatch);

        if (!passwordMatch) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String token = jwtUtils.generateToken(user);
        return ResponseEntity.ok(new LoginResponse(token, user.getUserEmail(), user.getRole()));
    }
}



