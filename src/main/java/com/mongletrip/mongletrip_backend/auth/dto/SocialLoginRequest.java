// src/main/java/com/mongletrip/mongletrip_backend/auth/dto/SocialLoginRequest.java

package com.mongletrip.mongletrip_backend.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SocialLoginRequest {
    /**
     * 소셜 로그인 플랫폼(Naver/Kakao)에서 발급받은 Access Token 또는 인가 코드를 받습니다.
     */
    private String accessToken;
}