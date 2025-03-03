package com.example.pongdang.user.controller;

import com.example.pongdang.user.vo.KakaoUserResponse;
import com.example.pongdang.user.service.UserService;
import com.example.pongdang.user.vo.KakaoTokenResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/kakao")
@RequiredArgsConstructor
public class KakaoAuthController {

    private final UserService userService; // 사용자 정보 저장 서비스
    private final RestTemplate restTemplate = new RestTemplate(); // HTTP 요청 수행

    private final String KAKAO_REST_API_KEY = "950a753d3092508b28b42ad997e4f987";
    private final String REDIRECT_URI = "http://localhost:3000/api/auth/kakao/callback"; // ✅ 프론트엔드 Redirect URI

    @PostMapping("/login") // ✅ 프론트엔드에서 인가 코드를 전달받는 엔드포인트
    public ResponseEntity<Map<String, String>> kakaoLogin(@RequestBody Map<String, String> request) {
        String code = request.get("code"); // 프론트엔드에서 보낸 인가 코드
        System.out.println("카카오에서 받은 code: " + code);

        String tokenUrl = "https://kauth.kakao.com/oauth/token"; // 카카오 토큰 요청 URL
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 토큰 요청을 위한 데이터 설정
        String body = "grant_type=authorization_code"
                + "&client_id=" + KAKAO_REST_API_KEY
                + "&redirect_uri=" + REDIRECT_URI  // ✅ 프론트엔드와 동일한 `redirect_uri`
                + "&code=" + code;

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<KakaoTokenResponse> tokenResponse = restTemplate.exchange(tokenUrl, HttpMethod.POST, requestEntity, KakaoTokenResponse.class);

        // 액세스 토큰이 없으면 로그인 실패
        if (tokenResponse.getBody() == null || tokenResponse.getBody().getAccess_token() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String accessToken = tokenResponse.getBody().getAccess_token();
        System.out.println("카카오 액세스 토큰: " + accessToken);

        // 사용자 정보 요청
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> userRequest = new HttpEntity<>(userHeaders);
        ResponseEntity<KakaoUserResponse> userResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userRequest, KakaoUserResponse.class);

        // 사용자 정보를 가져오지 못한 경우
        if (userResponse.getBody() == null || userResponse.getBody().getKakao_account() == null
                || userResponse.getBody().getKakao_account().getProfile() == null
                || userResponse.getBody().getKakao_account().getProfile().getNickname() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // 사용자 닉네임 가져오기
        String nickname = userResponse.getBody().getKakao_account().getProfile().getNickname();
        System.out.println("카카오에서 받은 닉네임: " + nickname);

        userService.saveUser(nickname); // 닉네임 저장

        // JSON 응답 반환
        Map<String, String> response = new HashMap<>();
        response.put("nickname", nickname);

        return ResponseEntity.ok(response);
    }
}
