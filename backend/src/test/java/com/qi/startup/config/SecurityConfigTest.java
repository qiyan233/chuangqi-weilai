package com.qi.startup.config;

import com.qi.startup.util.JwtUtil;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityConfigTest {

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void jwtFilterAuthenticatesValidBearerToken() throws ServletException, IOException {
        JwtUtil jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "startup-incubator-secret-key-2026-very-long-secret");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);

        String token = jwtUtil.generateToken(1L, "admin", "ADMIN");
        SecurityConfig.JwtAuthenticationFilter filter = new SecurityConfig.JwtAuthenticationFilter(jwtUtil);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);

        filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo("admin");
        assertThat(SecurityContextHolder.getContext().getAuthentication().getAuthorities())
            .extracting("authority")
            .containsExactly("ROLE_ADMIN");
        assertThat(request.getAttribute("userId")).isEqualTo(1L);
    }
}
