package com.mongletrip.mongletrip_backend.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 인증되지 않은 사용자가 보호된 리소스에 접근할 때 호출됩니다.
@Component
public class DelegatedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // 401 Unauthorized 오류가 났을 때 처리하는 로직입니다.
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {

        // 401 Unauthorized 응답을 클라이언트에게 보냅니다.
        // Postman에서 401을 받게 되며, 자세한 오류 메시지는 포함하지 않습니다.
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthenticated");
    }
}