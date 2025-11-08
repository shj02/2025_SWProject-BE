// src/main/java/com/mongletrip/mongletrip_backend/auth/dto/LoginResponse.java

package com.mongletrip.mongletrip_backend.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    /**
     * 인증에 성공하면 발급되는 JWT 토큰
     */
    private String token;

    /**
     * 로그인한 사용자의 고유 ID
     */
    private Long userId;

    /**
     * 초기 프로필 및 여행 스타일 선택까지 완료했는지 여부
     */
    private boolean isRegistered;
}