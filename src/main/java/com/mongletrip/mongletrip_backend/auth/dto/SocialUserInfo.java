// src/main/java/com/mongletrip/mongletrip_backend/auth/dto/SocialUserInfo.java

package com.mongletrip.mongletrip_backend.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SocialUserInfo {

    /**
     * 소셜 플랫폼에서 제공하는 고유 ID (예: 카카오 id, 네이버 id)
     */
    private String socialId;

    /**
     * 소셜 플랫폼 계정 이메일 (동의 안 받으면 null일 수 있음)
     */
    private String email;
}
