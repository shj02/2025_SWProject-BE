// src/main/java/com/mongletrip/mongletrip_backend/common/jwt/JwtTokenProvider.java

package com.mongletrip.mongletrip_backend.common.jwt;

import org.springframework.stereotype.Component;

/**
 * JWT 토큰 발급 및 검증 로직을 담당하는 클래스 (임시 구현)
 * Spring Security 설정이 완료되면 실제 JWT 로직으로 교체되어야 합니다.
 */
@Component
public class JwtTokenProvider {

    // 임시 토큰 발급 메서드: userId를 받아 가짜 토큰 문자열을 반환합니다.
    public String createToken(String userId) {
        // 실제로는 토큰에 유저 ID, 만료 시간 등을 암호화하여 저장해야 합니다.
        return "fake_jwt_token_for_user_" + userId;
    }
}