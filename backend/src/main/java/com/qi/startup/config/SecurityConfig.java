package com.qi.startup.config;

import com.qi.startup.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(request -> {
                var config = new org.springframework.web.cors.CorsConfiguration();
                config.setAllowCredentials(true);
                config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:5173"));
                config.setAllowedHeaders(Arrays.asList("*"));
                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setMaxAge(3600L);
                return config;
            }))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                .requestMatchers("/api/projects/approved").permitAll()
                .requestMatchers("/api/projects/{id}").permitAll()
                .requestMatchers("/api/stats/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/projects/*/review").hasRole("ADMIN")
                .requestMatchers("/api/applications/*/review").hasRole("ADMIN")
                .requestMatchers("/api/applications").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Component
    @RequiredArgsConstructor
    public static class JwtAuthenticationFilter extends OncePerRequestFilter {

        private final JwtUtil jwtUtil;

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException {
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtUtil.validateToken(token)) {
                    Long userId = jwtUtil.getUserIdFromToken(token);
                    String username = jwtUtil.getUsernameFromToken(token);
                    String role = jwtUtil.getRoleFromToken(token);

                    request.setAttribute("userId", userId);
                    request.setAttribute("username", username);
                    request.setAttribute("role", role);

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
                        var authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }

            filterChain.doFilter(request, response);
        }
    }
}
