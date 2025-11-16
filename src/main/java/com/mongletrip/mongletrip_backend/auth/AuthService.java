package com.mongletrip.mongletrip_backend.auth;

import com.mongletrip.mongletrip_backend.auth.dto.LoginResponse;
import com.mongletrip.mongletrip_backend.auth.dto.SocialUserInfo;
import com.mongletrip.mongletrip_backend.common.jwt.JwtTokenProvider;
import com.mongletrip.mongletrip_backend.domain.user.User;
import com.mongletrip.mongletrip_backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoUserService kakaoUserService;
    private final NaverUserService naverUserService;

    @Transactional
    public LoginResponse socialLogin(String provider, String accessToken) {

        // 1. 카카오/네이버에서 유저 정보 가져오기
        SocialUserInfo socialUserInfo;

        if ("NAVER".equalsIgnoreCase(provider)) {
            socialUserInfo = naverUserService.getUserInfo(accessToken);
        } else if ("KAKAO".equalsIgnoreCase(provider)) {
            socialUserInfo = kakaoUserService.getUserInfo(accessToken);
        } else {
            throw new IllegalArgumentException("지원하지 않는 소셜 로그인 플랫폼입니다: " + provider);
        }

        String socialId = socialUserInfo.getSocialId();
        String email = socialUserInfo.getEmail();

        // 2. DB에서 사용자 찾기 (순서: provider, socialId)
        Optional<User> existingUser =
                userRepository.findByProviderAndSocialId(provider, socialId);

        User user;
        boolean isRegistered;

        if (existingUser.isPresent()) {
            user = existingUser.get();
            isRegistered = user.isRegistered();
        } else {
            user = User.builder()
                    .socialId(socialId)
                    .provider(provider)
                    .email(email)
                    .isRegistered(false)
                    .build();
            userRepository.save(user);
            isRegistered = false;
        }

        // 3. JWT 발급
        String token = jwtTokenProvider.createToken(user.getId().toString());

        return LoginResponse.builder()
                .token(token)
                .userId(user.getId())
                .isRegistered(isRegistered)
                .build();
    }
}
