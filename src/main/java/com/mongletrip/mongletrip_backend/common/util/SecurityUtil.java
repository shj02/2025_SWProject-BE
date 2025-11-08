// src/main/java/com/mongletrip/mongletrip_backend/common/util/SecurityUtil.java

package com.mongletrip.mongletrip_backend.common.util;

// 이 클래스는 JWT 인증 로직이 Spring Security에 구현되었다고 가정하고,
// 현재 요청을 보낸 사용자(UserId)를 반환하는 역할을 합니다. (임시 구현)
public class SecurityUtil {

    public static Long getCurrentUserId() {
        // TODO: 실제 JWT/SecurityContext에서 사용자 ID를 추출하는 로직으로 변경해야 합니다.
        // 현재는 개발의 편의를 위해 임시로 1L을 반환합니다.
        return 1L;
    }
}