package com.example.pongdang.config;

import com.example.pongdang.user.entity.UserEntity;
import com.example.pongdang.user.provider.JwtTokenProvider;
import com.example.pongdang.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@Component // Spring이 관리하는 Bean으로 등록
@RequiredArgsConstructor
public class JwtCookieAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // ✅ 쿠키에서 JWT 가져오기
        Optional<Cookie> jwtCookie = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> "jwt".equals(cookie.getName()))
                .findFirst();

        if (jwtCookie.isPresent()) {
            String jwtToken = jwtCookie.get().getValue();

            // ✅ JWT 검증
            if (jwtProvider.validateToken(jwtToken)) {
                String email = jwtProvider.getEmailFromToken(jwtToken);

                // ✅ `UserRepository`를 직접 사용하여 사용자 조회
                Optional<UserEntity> optionalUser = userRepository.findByEmail(email);
                if (optionalUser.isEmpty()) {
                    filterChain.doFilter(request, response);
                    return;
                }

                UserEntity userEntity = optionalUser.get();

                // ✅ `getAuthorities()` 대신 빈 리스트 전달
                UserDetails userDetails = new User(userEntity.getEmail(), "", Collections.emptyList());

                // ✅ Spring Security 인증 객체 생성
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // ✅ 인증 정보 SecurityContext에 저장
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
