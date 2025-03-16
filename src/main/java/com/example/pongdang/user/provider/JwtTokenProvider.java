package com.example.pongdang.user.provider;
import lombok.extern.slf4j.Slf4j;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.SignatureException;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;

    // ✅ application.yml의 jwt.secret 값을 가져옴
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalArgumentException("❌ JWT SECRET_KEY가 설정되지 않았습니다!");
        }
        System.out.println("✅ 설정된 JWT SECRET_KEY: " + secretKey); // 🔥 SECRET_KEY 확인 로그 추가
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // JWT 생성
    public String createToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간 후 만료
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ JWT 검증
    public boolean validateToken(String token) {
        if (token == null || token.isBlank()) {
            log.warn("❌ JWT 토큰이 비어있거나 null입니다.");
            return false;
        }

        // ✅ "Bearer " 접두사 제거
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key) // 🔍 서명 검증
                    .build()
                    .parseClaimsJws(token);

            log.info("✅ JWT 검증 성공! Subject: {}", claims.getBody().getSubject());
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("❌ JWT 토큰이 만료되었습니다.");
        } catch (MalformedJwtException e) {
            log.warn("❌ JWT 토큰 형식이 올바르지 않습니다.");
        } catch (UnsupportedJwtException e) {
            log.warn("❌ JWT 토큰이 지원되지 않습니다.");
        } catch (SignatureException e) {
            log.warn("❌ JWT 서명 검증 실패! (SECRET_KEY가 다를 가능성이 큼)");
        } catch (JwtException e) {
            log.warn("❌ JWT 검증 실패: {}", e.getMessage());
        } catch (Exception e) {
            log.warn("❌ 예상치 못한 JWT 오류 발생: {}", e.getMessage());
        }
        return false;
    }




    // ✅ 토큰에서 이메일 추출
    public String getEmailFromToken(String token) {
        if (token == null || token.isBlank()) {
            throw new RuntimeException("❌ JWT 토큰이 비어있거나 null입니다.");
        }

        // ✅ "Bearer " 접두사 제거
        if (token.startsWith("Bearer ")) {
            log.info("🔹 'Bearer ' 접두사 제거 전: {}", token);
            token = token.substring(7);
            log.info("🔹 'Bearer ' 접두사 제거 후: {}", token);
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("❌ JWT 토큰이 만료되었습니다.");
        } catch (JwtException e) {
            throw new RuntimeException("❌ JWT 토큰이 유효하지 않습니다. 원인: " + e.getMessage());
        }
    }


}
