// src/main/java/com/mongletrip/mongletrip_backend/user/UserRepository.java

package com.mongletrip.mongletrip_backend.user;

import com.mongletrip.mongletrip_backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // ... (기존에 정의된 메서드들)

    /**
     * API 0: 소셜 로그인 시, Provider와 ID를 이용해 사용자를 찾는 메서드입니다.
     * findBySocialIdAndProvider 오류를 해결하기 위해 추가합니다.
     */
    Optional<User> findBySocialIdAndProvider(String socialId, String provider);
}