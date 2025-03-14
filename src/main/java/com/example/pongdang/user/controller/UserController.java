package com.example.pongdang.user.controller;

import com.example.pongdang.user.dto.KakaoUserDto;
import com.example.pongdang.user.dto.UserDto;
import com.example.pongdang.user.entity.UserEntity;
import com.example.pongdang.user.repository.UserRepository;
import com.example.pongdang.user.service.UserService;
import com.example.pongdang.user.vo.ResponseUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.pongdang.user.provider.JwtTokenProvider;
import jakarta.servlet.http.Cookie;

import java.util.Arrays;
import java.util.Optional;

//@RestController
//@RequestMapping("/api/v1/user")
//public class UserController {
//
//    private final UserService userService;
//
//    public UserController(UserService userService){
//        this.userService = userService;
//    }
//
//    // 사용자 저장
//    @PostMapping
//    public ResponseUser saveUser(@RequestBody UserDto userDto){
//
//        UserEntity userEntity = userService.saveUser(userDto);
//
//        return ResponseUser.builder()
//                .email(userDto.getEmail())
//                .nickname(userDto.getNickname())
//                .build();
//    }
//
//    // 특정 사용자 조회
////    @GetMapping("/{id}")
////    public ResponseEntity<ResponseUser> getFishingTripById(@PathVariable Long id) {
////        ResponseUser user = userService.getUserById(id);
////        return ResponseEntity.ok(user);
////    }
//}


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;  // ✅ 추가
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

    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> "jwt".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        if (token == null || !jwtProvider.validateToken(token)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String email = jwtProvider.getEmailFromToken(token);
        Optional<UserEntity> user = userRepository.findByEmail(email);

        return user.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body("User not found"));
    }

}