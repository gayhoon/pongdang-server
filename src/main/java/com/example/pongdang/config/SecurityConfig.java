package com.example.pongdang.config;

import com.example.pongdang.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtCookieAuthFilter jwtCookieAuthFilter;
    private final UserService userService; // UserService 활용
    private final CorsConfigurationSource corsConfigurationSource; // ✅ CorsConfig 사용

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()) // iframe 허용 (같은 도메인)
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource)) // ✅ CORS 설정 적용
                .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()  // 로그인, 회원가입 허용
                        .requestMatchers("/api/v1/user/**").authenticated() // 인증 필요
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtCookieAuthFilter, UsernamePasswordAuthenticationFilter.class) // 필터 추가
                .formLogin(form -> form.disable()) // 기본 로그인 비활성화
                .httpBasic(httpBasic -> httpBasic.disable()); // HTTP 기본 인증 비활성화

        return http.build();
    }
}
