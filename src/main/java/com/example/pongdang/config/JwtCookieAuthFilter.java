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

        System.out.println("🔥 JwtCookieAuthFilter 실행됨!");

        String jwtToken = null;

        // ✅ 1. 쿠키에서 JWT 가져오기
        Optional<Cookie> jwtCookie = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> "jwt".equals(cookie.getName()))
                .findFirst();

        if (jwtCookie.isPresent()) {
            jwtToken = jwtCookie.get().getValue();
        }

        // ✅ 2. Safari 대응: Authorization 헤더에서도 JWT 확인 (쿠키가 없을 경우)
        if (jwtToken == null) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwtToken = authHeader.substring(7);
            }
        }

        // ✅ 3. JWT 검증 및 사용자 정보 설정
        if (jwtToken != null && jwtProvider.validateToken(jwtToken)) {
            System.out.println("✅ 추출된 JWT: " + jwtToken);
            String email = jwtProvider.getEmailFromToken(jwtToken);

            Optional<UserEntity> optionalUser = userRepository.findByEmail(email);
            System.out.println("✅ 사용자 조회 결과: " + optionalUser);
            if (optionalUser.isPresent()) {
                UserEntity userEntity = optionalUser.get();

                UserDetails userDetails = new User(
                        userEntity.getEmail(),
                        "",
                        Collections.emptyList()
                );

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                System.out.println("✅ 사용자 인증 토큰 생성 완료: " + authToken);
                System.out.println("✅ 인증 정보 설정 전: " + SecurityContextHolder.getContext().getAuthentication());
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("✅ 인증 정보 설정 후: " + SecurityContextHolder.getContext().getAuthentication());
            }
        }

        filterChain.doFilter(request, response);
    }
}
