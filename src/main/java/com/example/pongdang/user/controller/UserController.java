package com.example.pongdang.user.controller;

import com.example.pongdang.user.dto.KakaoUserDto;
import com.example.pongdang.user.entity.UserEntity;
import com.example.pongdang.user.service.UserService;
import com.example.pongdang.user.vo.ResponseUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 사용자 저장
    @PostMapping
    public ResponseEntity<ResponseUser> saveUser(@RequestBody KakaoUserDto kakaoUserDto) {
        UserEntity userEntity = userService.saveUser(kakaoUserDto);

        ResponseUser responseUser = ResponseUser.builder()
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .build();

        return ResponseEntity.ok(responseUser);
    }

    // ✅ 회원정보 조회 (서비스로 위임)
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        ResponseUser responseUser = userService.getUserInfo(request);
        return ResponseEntity.ok(responseUser);
    }

    // ✅ 회원탈퇴 (서비스로 위임)
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteUser(HttpServletRequest request, HttpServletResponse response) {
        userService.deleteUser(request, response);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }

    // ✅ 닉네임 변경 API (서비스로 위임)
    @PatchMapping("/updateNickname")
    public ResponseEntity<String> updateNickname(@RequestParam("nickname") String newNickname, HttpServletRequest request) {
        userService.updateNickname(request, newNickname);
        return ResponseEntity.ok("닉네임이 성공적으로 변경되었습니다.");
    }

    // ✅ 프로필 이미지 업로드 API
    @PostMapping("/uploadProfileImage")
    public ResponseEntity<String> uploadProfileImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String imageUrl = userService.uploadProfileImage(file, request);
        return ResponseEntity.ok(imageUrl);
    }
}