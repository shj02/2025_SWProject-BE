package com.mongletrip.mongletrip_backend.common.jwt;

import com.mongletrip.mongletrip_backend.common.util.SecurityUtil;
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

// 이 필터는 HTTP 요청이 들어올 때마다 JWT 토큰을 확인하고 인증 정보를 설정합니다.
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // 토큰의 유효성을 검증하는 로직이 필요하지만, 여기서는 SecurityUtil을 사용해 임시 인증합니다.
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 헤더에서 토큰을 추출합니다.
        String token = resolveToken(request);

        // 2. 토큰이 존재하고 유효하다고 가정하고, 임시 사용자 ID로 인증을 진행합니다.
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            // 실제 구현에서는 JWT 토큰을 파싱하여 사용자 ID를 추출해야 하지만,
            // 현재는 SecurityUtil의 임시 ID(1L)를 사용하여 인증합니다.

            // SecurityUtil.getCurrentUserId()가 1L을 반환하도록 설정되어 있으므로,
            // 모든 유효한 토큰은 임시적으로 'User ID 1'로 인증됩니다.
            Long userId = SecurityUtil.getCurrentUserId();

            if (userId != null) {
                // 임시 인증 객체 생성 (권한은 빈 목록으로 설정)
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userId, // Principal (사용자 ID)
                        null,   // Credentials (패스워드)
                        Collections.emptyList() // Authorities (권한)
                );

                // SecurityContext에 인증 정보 설정
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    // HTTP 헤더에서 JWT 토큰을 추출합니다.
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}