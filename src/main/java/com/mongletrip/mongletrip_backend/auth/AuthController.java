// src/main/java/com/mongletrip/mongletrip_backend/auth/AuthController.java

package com.mongletrip.mongletrip_backend.auth;

import com.mongletrip.mongletrip_backend.auth.dto.LoginResponse;
import com.mongletrip.mongletrip_backend.auth.dto.SocialLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * API 0: 네이버 소셜 로그인 및 회원가입 처리 (POST /api/auth/naver)
     */
    @PostMapping("/naver")
    public ResponseEntity<LoginResponse> naverLogin(@RequestBody SocialLoginRequest request) {
        // NAVER 플랫폼 코드를 넘겨 처리합니다.
        LoginResponse response = authService.socialLogin("NAVER", request.getAccessToken());
        return ResponseEntity.ok(response);
    }

    /**
     * API 0: 카카오 소셜 로그인 및 회원가입 처리 (POST /api/auth/kakao)
     */
    @PostMapping("/kakao")
    public ResponseEntity<LoginResponse> kakaoLogin(@RequestBody SocialLoginRequest request) {
        // KAKAO 플랫폼 코드를 넘겨 처리합니다.
        LoginResponse response = authService.socialLogin("KAKAO", request.getAccessToken());
        return ResponseEntity.ok(response);
    }

    /**
     * API 35: 로그아웃 (POST /api/auth/logout)
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // 실제로는 서버 측에서 JWT 블랙리스트 처리 또는 세션 무효화 로직이 필요할 수 있습니다.
        // 여기서는 클라이언트에게 토큰 삭제를 유도하며 200 OK를 반환합니다.
        return ResponseEntity.ok().build();
    }
}