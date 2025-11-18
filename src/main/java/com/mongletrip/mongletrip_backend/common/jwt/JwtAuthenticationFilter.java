// src/main/java/com/mongletrip/mongletrip_backend/common/jwt/JwtAuthenticationFilter.java

package com.mongletrip.mongletrip_backend.common.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Authorization 헤더에서 토큰 추출
        String token = resolveToken(request); // 예: "fake_jwt_token_for_user_12"

        if (token != null) {
            // 2. 편법: fake 토큰에서 userId만 파싱
            Long userId = extractUserIdFromFakeToken(token);

            if (userId != null) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userId, // principal
                        null,
                        Collections.emptyList()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            // "Bearer " 이후 문자열만 반환
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    /**
     * "fake_jwt_token_for_user_12" 형식에서 마지막 숫자 부분(12)을 userId로 파싱
     */
    private Long extractUserIdFromFakeToken(String token) {
        if (token == null) {
            return null;
        }
        int idx = token.lastIndexOf("_");
        if (idx == -1 || idx == token.length() - 1) {
            return null;
        }
        String idPart = token.substring(idx + 1);
        try {
            return Long.parseLong(idPart);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
