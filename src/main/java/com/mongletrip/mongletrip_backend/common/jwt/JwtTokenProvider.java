// src/main/java/com/mongletrip/mongletrip_backend/common/jwt/JwtTokenProvider.java

package com.mongletrip.mongletrip_backend.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct; // 👈 javax로 되어 있는지 확인해주세요.
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKeyString;

    @Value("${jwt.token-valid-time}")
    private long tokenValidTime;

    private Key secretKey;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 실제 JWT 토큰을 생성하는 메소드
     */
    public String createToken(String userId) {
        Claims claims = Jwts.claims().setSubject(userId);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰에서 회원 정보(userId)를 추출하는 메소드
     */
    public String getUserId(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * 토큰의 유효성 + 만료일자를 확인하는 메소드
     */
    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtToken);
            return true;
        } catch (Exception e) {
            // 유효하지 않은 토큰일 경우 false를 반환합니다.
            return false;
        }
    }
}