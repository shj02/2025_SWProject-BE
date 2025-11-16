// src/main/java/com/mongletrip/mongletrip_backend/auth/NaverUserService.java

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
public class NaverUserService {

    private final WebClient webClient;

    @Value("${social.naver.user-info-uri}")
    private String userInfoUri;

    /**
     * 네이버 Access Token 으로 네이버 유저 정보 조회
     */
    public SocialUserInfo getUserInfo(String accessToken) {

        Map<String, Object> response = webClient.get()
                .uri(userInfoUri)
                .headers(headers -> headers.set("Authorization", "Bearer " + accessToken))
                .retrieve()
                .bodyToMono(Map.class)
                .onErrorResume(e -> Mono.error(new RuntimeException("네이버 사용자 정보 조회 실패", e)))
                .block();

        if (response == null || !"00".equals(response.get("resultcode"))) {
            throw new RuntimeException("네이버 사용자 정보 응답이 올바르지 않습니다: " + response);
        }

        Object respObj = response.get("response");
        if (!(respObj instanceof Map)) {
            throw new RuntimeException("네이버 response 필드가 없습니다: " + response);
        }

        Map<String, Object> profile = (Map<String, Object>) respObj;

        String socialId = profile.get("id").toString();
        String email = profile.get("email") != null ? profile.get("email").toString() : null;

        return new SocialUserInfo(socialId, email);
    }
}
