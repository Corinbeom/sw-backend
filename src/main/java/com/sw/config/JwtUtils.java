package com.sw.config;


import com.sw.model.User;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {


    private long expirationMs; //  토큰 만료 시간

    private String SECRET_KEY;

    public JwtUtils() {
        Dotenv dotenv = Dotenv.load();
        this.expirationMs = Long.parseLong(dotenv.get("JWT_EXPIRATION_MS"));
        this.SECRET_KEY = dotenv.get("JWT_SECRET");
    }

    private Key getSignKey() {
        byte[] ketByte = Decoders.BASE64.decode(SECRET_KEY);
        return new SecretKeySpec(ketByte, SignatureAlgorithm.HS512.getJcaName());
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("nickName", user.getNickName())
                .claim("email", user.getUserEmail())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSignKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // userEmail 추출
    public String extractUserEmail(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();
            return claims.getSubject(); // subject에서 userEmail 추출
        } catch (Exception e) {
            log.error("토큰에서 사용자 이메일 추출 에러: {}", e.getMessage());
            return null;
        }
    }

    // nickName 추출
    public String extractNickName(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();
            return claims.get("nickName", String.class);
        } catch (Exception e) {
            log.error("토큰에서 사용자 닉네임 추출 에러: {}", e.getMessage());
            return null;
        }
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token.replace("Bearer ", ""));
            return true;
        } catch (MalformedJwtException ex) {
            log.error("유효하지 않은 JWT 토큰: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("만료된 JWT 토큰: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("지원되지 않는 JWT 토큰: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT 클레임 문자열이 비어 있음: {}", ex.getMessage());
        }
        return false;
    }
}
