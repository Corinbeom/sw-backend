package com.sw.service;

import com.sw.model.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailService {


    private final JavaMailSender mailSender;

    private final Map<String, String> emailVerificationMap = new ConcurrentHashMap<>();

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(User user) {
        String verificationUrl = "http://localhost:8080/api/auth/verify?token=" + user.getVerificationToken();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getUserEmail());
        message.setSubject("Email Verification");
        message.setText("Please click the link to verify your email: " + verificationUrl);

        mailSender.send(message);
    }

    //  이메일로 인증번호 전송 메소드
    public String sendVerificationCode(String email) {
        String verificationCode = String.format("%06d", new Random().nextInt(999999));
        emailVerificationMap.put(email, verificationCode);

        // Send email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email Verification Code");
        message.setText("회원가입 인증번호 입니다: " + verificationCode);
        mailSender.send(message);

        System.out.println("인증번호 : "+verificationCode);
        return verificationCode;
    }

    //  인증번호 검증
    public boolean verifyCode(String email, String code) {
        String storedCode = emailVerificationMap.get(email);
        return storedCode != null && storedCode.equals(code);
    }
}
