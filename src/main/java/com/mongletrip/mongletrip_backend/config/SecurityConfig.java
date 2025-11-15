package com.mongletrip.mongletrip_backend.config;

import com.mongletrip.mongletrip_backend.common.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 🚨 CorsConfigurationSource import 추가
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final DelegatedAuthenticationEntryPoint delegatedAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // 🚨 1. CORS 설정 적용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 🚨 2. CSRF 보호 명시적 비활성화 (POST, PUT 요청을 허용하기 위해 필수)
                .csrf(AbstractHttpConfigurer::disable)

                // 세션을 사용하지 않음 (JWT 기반 인증이므로 STATELESS)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 인증 실패 처리 (401 에러)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(delegatedAuthenticationEntryPoint)
                )
                // HTTP 요청에 대한 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 로그인/회원가입 경로 (/api/auth로 시작하는 모든 경로)는 인증 없이 접근 허용
                        .requestMatchers("/api/auth/**").permitAll()
                        // 커뮤니티 게시글 목록 조회 및 상세 조회는 인증 없이 접근 허용 (비회원 접근 허용)
                        .requestMatchers("/api/community/posts", "/api/community/posts/*").permitAll()
                        // 나머지 모든 요청은 인증(토큰) 필요
                        .anyRequest().authenticated()
                )
                // JWT 필터 등록: UsernamePasswordAuthenticationFilter 이전에 실행되도록 설정
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    // 🚨 CORS(Cross-Origin Resource Sharing) 설정을 위한 Bean
    // Postman 테스트와 실제 프론트엔드 연결을 위해 필요합니다.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 모든 출처(Origin) 허용
        configuration.setAllowedOrigins(List.of("*"));
        // 모든 HTTP 메서드 허용 (GET, POST, PUT, DELETE 등)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        // 모든 헤더 허용
        configuration.setAllowedHeaders(List.of("*"));
        // 자격 증명(쿠키, 인증 헤더) 허용 여부 (토큰 기반이므로 false로 설정 가능)
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 모든 경로에 위 CORS 설정을 적용
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}