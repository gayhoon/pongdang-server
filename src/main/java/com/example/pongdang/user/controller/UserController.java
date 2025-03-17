package com.example.pongdang.user.controller;

import com.example.pongdang.user.dto.KakaoUserDto;
import com.example.pongdang.user.dto.UserDto;
import com.example.pongdang.user.entity.UserEntity;
import com.example.pongdang.user.repository.UserRepository;
import com.example.pongdang.user.service.UserService;
import com.example.pongdang.user.vo.ResponseUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.pongdang.user.provider.JwtTokenProvider;
import jakarta.servlet.http.Cookie;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;  // 추가
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtProvider;

    @PostMapping
    public ResponseEntity<ResponseUser> saveUser(@RequestBody KakaoUserDto kakaoUserDto) {
        UserEntity userEntity = userService.saveUser(kakaoUserDto);

        ResponseUser responseUser = ResponseUser.builder()
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .build();

        return ResponseEntity.ok(responseUser);
    }

    // 회원정보 조회
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            System.err.println("🚨 쿠키 없음!");
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // 디버깅: 모든 쿠키 출력
        for (Cookie cookie : cookies) {
            System.out.println("🍪 쿠키 이름: " + cookie.getName() + " | 값: " + cookie.getValue());
        }

        // JWT 쿠키 찾기
        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> "jwt".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        if (token == null) {
            System.err.println("🚨 JWT 쿠키 없음!");
            return ResponseEntity.status(401).body("Unauthorized");
        }

        if (!jwtProvider.validateToken(token)) {
            System.err.println("🚨 JWT 검증 실패!");
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // JWT에서 이메일 추출 후 DB 조회
        String email = jwtProvider.getEmailFromToken(token);
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        UserEntity userEntity = userOptional.get();
        System.out.println("🔍 DB에서 찾은 사용자: " + userEntity);

        // **DTO로 변환 후 반환 (순환 참조 방지)**
        ResponseUser responseUser = ResponseUser.builder()
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .build();

        return ResponseEntity.ok(responseUser);
    }

    // 회원탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteUser(HttpServletRequest request, HttpServletResponse response) {
        // JWT 쿠키에서 토큰 가져오기
        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> "jwt".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        // 토큰 검증
        if (token == null || !jwtProvider.validateToken(token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // JWT에서 **이메일**을 가져와 회원 ID 조회
        String email = jwtProvider.getEmailFromToken(token);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 회원 삭제 (ID를 넘겨줌)
        userService.deleteUser(user.getId());

        // JWT 쿠키 삭제
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }
}