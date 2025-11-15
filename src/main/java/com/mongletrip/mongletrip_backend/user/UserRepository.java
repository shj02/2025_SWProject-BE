// src/main/java/com/mongletrip/mongletrip_backend/user/UserRepository.java

package com.mongletrip.mongletrip_backend.user;

import com.mongletrip.mongletrip_backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 소셜 로그인 시, Provider와 ID를 이용해 사용자를 찾습니다.
     * @param socialProvider 소셜 플랫폼 (NAVER, KAKAO 등)
     * @param socialId 해당 플랫폼의 고유 ID
     * @return User 객체 (Optional)
     */
    Optional<User> findBySocialProviderAndSocialId(String socialProvider, String socialId);
}