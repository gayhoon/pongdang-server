package com.example.pongdang.user.controller;

import com.example.pongdang.user.dto.KakaoUserDto;
import com.example.pongdang.user.entity.UserEntity;
import com.example.pongdang.user.service.UserService;
import com.example.pongdang.user.provider.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtProvider;
    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody Map<String, String> request, HttpServletResponse response) {
        String code = request.get("code");

        System.out.println("카카오 로그인 요청, code: " + code);

        try {
            // 1️⃣ 백엔드에서 카카오 `access_token` 요청
            String accessToken = getKakaoAccessToken(code);

            // 2️⃣ `access_token`을 사용해 카카오 사용자 정보 요청
            KakaoUserDto kakaoUser = getKakaoUserInfo(accessToken);

            // 3️⃣ 사용자 정보 저장 (기존 사용자면 조회)
            UserEntity user = userService.saveUser(kakaoUser);

            // 4️⃣ JWT 생성
            String jwtToken = jwtProvider.createToken(user.getEmail());

            // 5️⃣ HTTP-only 쿠키로 JWT 저장 (보안 강화)
            Cookie cookie = new Cookie("jwt", jwtToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // HTTPS 환경에서는 true
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60); // 1시간
            response.addCookie(cookie);

            // JSON 형식으로 응답 반환
            Map<String, String> responseBody = Map.of(
                    "message", "Login successful",
                    "jwt", jwtToken,  // ✅ 응답에 JWT 추가!
                    "email", user.getEmail(),
                    "nickname", user.getNickname()
            );

            return ResponseEntity.ok(responseBody);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("카카오 로그인 실패");
        }
    }

    // 🔹 카카오 `access_token` 요청
    private String getKakaoAccessToken(String code) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=authorization_code"
                + "&client_id=9cad215e09b7aa7a5b7143e4e4b48a0a"
                + "&redirect_uri=http://localhost:3000/auth/callback"
                + "&code=" + code;

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://kauth.kakao.com/oauth/token", request, Map.class
        );

        System.out.println("🔹 카카오 access_token 요청 응답: " + response.getBody()); // 로그 추가

        return (String) response.getBody().get("access_token");
    }


    // 🔹 `access_token`을 이용해 카카오 사용자 정보 요청
    private KakaoUserDto getKakaoUserInfo(String accessToken) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                request,
                Map.class
        );

        System.out.println("🔹 카카오 API 응답: " + response.getBody()); // 전체 응답 확인

        Map<String, Object> kakaoAccount = (Map<String, Object>) response.getBody().get("kakao_account");
        if (kakaoAccount == null) {
            System.out.println("⚠️ kakao_account가 null입니다!");
            return new KakaoUserDto(null, null);
        }

        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        if (profile == null) {
            System.out.println("⚠️ profile이 null입니다!");
            return new KakaoUserDto(null, null);
        }

        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");

        System.out.println("🎯 가져온 사용자 정보: email=" + email + ", nickname=" + nickname);

        return new KakaoUserDto(email, nickname);
    }

}
