// src/main/java/com/mongletrip/mongletrip_backend/auth/AuthService.java

package com.mongletrip.mongletrip_backend.auth;

import com.mongletrip.mongletrip_backend.auth.dto.LoginResponse;
import com.mongletrip.mongletrip_backend.common.jwt.JwtTokenProvider;
import com.mongletrip.mongletrip_backend.domain.user.User;
import com.mongletrip.mongletrip_backend.user.UserRepository; // user 패키지의 Repository 사용
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    // private final SocialApiClient socialApiClient; // 실제로는 외부 API 통신을 위한 클라이언트 필요

    /**
     * 소셜 로그인 플랫폼의 Access Token을 이용해 사용자를 인증하고 JWT 토큰을 발급합니다.
     * @param provider 소셜 플랫폼 이름 ("NAVER", "KAKAO")
     * @param accessToken 소셜 플랫폼에서 받은 Access Token
     * @return 로그인 응답 (JWT 토큰, 사용자 ID, 가입 완료 여부)
     */
    @Transactional
    public LoginResponse socialLogin(String provider, String accessToken) {

        // 1. 소셜 로그인 플랫폼에서 사용자 정보 가져오기 (시뮬레이션 부분)
        // 실제 구현에서는 accessToken을 이용해 Naver/Kakao의 유저 정보 API를 호출해야 합니다.
        String socialId;
        String email;

        // --- [주의: 이 부분은 실제 API 호출이 아닌 시뮬레이션 코드입니다.] ---
        if ("NAVER".equals(provider)) {
            socialId = "NAVER_12345";
            email = "naver_user@example.com";
        } else if ("KAKAO".equals(provider)) {
            socialId = "KAKAO_67890";
            email = "kakao_user@example.com";
        } else {
            throw new IllegalArgumentException("지원하지 않는 소셜 로그인 플랫폼입니다: " + provider);
        }
        // -------------------------------------------------------------------

        // 2. DB에서 기존 사용자 찾기 (User Repository에 findBySocialIdAndProvider 메서드가 필요함)
        Optional<User> existingUser = userRepository.findBySocialIdAndProvider(socialId, provider);
        User user;
        boolean isRegistered;

        if (existingUser.isPresent()) {
            // 3-1. 기존 사용자: 로그인 처리
            user = existingUser.get();
            isRegistered = user.isRegistered();
        } else {
            // 3-2. 신규 사용자: 임시 회원가입 처리
            user = User.builder()
                    .socialId(socialId)
                    .provider(provider)
                    .email(email)
                    .isRegistered(false) // 초기 가입 상태는 미완료
                    .build();
            userRepository.save(user);
            isRegistered = false;
        }

        // 4. JWT 토큰 생성 (JwtTokenProvider의 createToken 메서드 사용)
        String token = jwtTokenProvider.createToken(user.getId().toString());

        // 5. 응답 DTO 반환
        return LoginResponse.builder()
                .token(token)
                .userId(user.getId())
                .isRegistered(isRegistered)
                .build();
    }
}