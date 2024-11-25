package com.sw.config;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;


public class JwtUtils {

    @Value("${jwt.expiration.ms}")
    private long expirationMs; //  토큰 만료 시간

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private Key getSignKey() {
        byte[] ketByte = Decoders.BASE64.decode(SECRET_KEY);
        return new SecretKeySpec(ketByte, SignatureAlgorithm.HS512.getJcaName());
    }

    public String generateToken(String userId, Long Id, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(String.valueOf(Id))
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSignKey())
                .compact();
    }
}
