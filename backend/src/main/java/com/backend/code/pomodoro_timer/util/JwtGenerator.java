package com.backend.code.pomodoro_timer.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtGenerator {

    @Value("${app.jwt.secret}")
    private String secretKey;

    public String generateToken(String username) {

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(username)
                .signWith(key)
                .compact();
    }
}
