// src/main/java/com/mongletrip/mongletrip_backend/auth/KakaoUserService.java

package com.mongletrip.mongletrip_backend.auth;

import com.mongletrip.mongletrip_backend.auth.dto.SocialUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoUserService {

    private final WebClient webClient;

    @Value("${social.kakao.user-info-uri}")
    private String userInfoUri;

    /**
     * 카카오 Access Token 으로 카카오 유저 정보 조회
     */
    public SocialUserInfo getUserInfo(String accessToken) {

        Map<String, Object> response = webClient.get()
                .uri(userInfoUri)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(Map.class)
                .onErrorResume(e -> Mono.error(new RuntimeException("카카오 사용자 정보 조회 실패", e)))
                .block();

        if (response == null || response.get("id") == null) {
            throw new RuntimeException("카카오 사용자 정보 응답이 올바르지 않습니다: " + response);
        }

        // id
        String socialId = String.valueOf(response.get("id"));

        // kakao_account.email
        String email = null;
        Object kakaoAccountObj = response.get("kakao_account");
        if (kakaoAccountObj instanceof Map) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) kakaoAccountObj;
            Object emailObj = kakaoAccount.get("email");
            if (emailObj != null) {
                email = emailObj.toString();
            }
        }

        return new SocialUserInfo(socialId, email);
    }
}
