// src/main/java/com/mongletrip/mongletrip_backend/common/util/SecurityUtil.java

package com.mongletrip.mongletrip_backend.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {

    /**
     * SecurityContext에 저장된 Authentication에서 userId(Long)를 꺼낸다.
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof Long) {
            return (Long) principal;
        }

        if (principal instanceof String) {
            try {
                return Long.parseLong((String) principal);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }
}
