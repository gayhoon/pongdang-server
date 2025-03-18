package com.example.pongdang.user.service;

import com.example.pongdang.user.dto.KakaoUserDto;
import com.example.pongdang.user.entity.UserEntity;
import com.example.pongdang.user.provider.JwtTokenProvider;
import com.example.pongdang.user.repository.UserRepository;
import com.example.pongdang.user.vo.ResponseUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import com.example.pongdang.fishingTrip.repository.FishingTripRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service // 이 클래스가 서비스 역할을 한다고 알려줌
@RequiredArgsConstructor // 자동으로 생성자를 만들어줌
public class UserService {

    private final UserRepository userRepository; // 데이터베이스를 다룰 수 있도록 연결
    private final FishingTripRepository fishingTripRepository; // 추가
    private final JwtTokenProvider jwtProvider;

    // ✅ [1] 카카오 로그인 사용자 저장 (회원가입)
    @Transactional
    public UserEntity saveUser(KakaoUserDto kakaoUser) {
        return userRepository.findByEmail(kakaoUser.getEmail())
                .orElseGet(() -> userRepository.save(UserEntity.builder()
                        .email(kakaoUser.getEmail())
                        .nickname(kakaoUser.getNickname())
                        .build()));
    }

    // ✅ [2] JWT 검증 후 사용자 정보 조회
    public ResponseUser getUserInfo(HttpServletRequest request) {
        Optional<UserEntity> optionalUser = getUserFromJwt(request);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Unauthorized: JWT가 유효하지 않거나 사용자 정보를 찾을 수 없습니다.");
        }
        UserEntity userEntity = optionalUser.get();
        return ResponseUser.builder()
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .profileImage(userEntity.getProfileImageUrl())
                .build();
    }

    // ✅ [3] JWT 검증 후 사용자 엔티티 조회
    private Optional<UserEntity> getUserFromJwt(HttpServletRequest request) {
        String email = jwtProvider.getEmailFromRequest(request);
        if (email == null) {
            return Optional.empty();
        }
        return userRepository.findByEmail(email);
    }

    // ✅ [5] 닉네임 변경 서비스
    @Transactional
    public void updateNickname(HttpServletRequest request, String newNickname) {
        Optional<UserEntity> optionalUser = getUserFromJwt(request);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Unauthorized: JWT가 유효하지 않거나 사용자 정보를 찾을 수 없습니다.");
        }
        if (newNickname == null || newNickname.trim().isEmpty()) {
            throw new IllegalArgumentException("닉네임은 비어 있을 수 없습니다.");
        }
        UserEntity user = optionalUser.get();
        user.setNickname(newNickname);
        userRepository.save(user);
    }

    // ✅ [6] 회원탈퇴 서비스
    @Transactional
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        Optional<UserEntity> optionalUser = getUserFromJwt(request);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Unauthorized: JWT가 유효하지 않거나 사용자 정보를 찾을 수 없습니다.");
        }
        UserEntity user = optionalUser.get();

        // 회원이 작성한 모든 게시글 삭제
        fishingTripRepository.deleteByAuthor(user);

        // 회원 삭제
        userRepository.delete(user);

        // JWT 쿠키 삭제
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    // ✅ 프로필 이미지 업로드 서비스
    public String uploadProfileImage(MultipartFile file, HttpServletRequest request) {
        String email = jwtProvider.getEmailFromRequest(request);
        if (email == null) {
            throw new RuntimeException("Unauthorized: JWT가 유효하지 않음");
        }

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // ✅ 📁 저장 경로를 절대 경로로 설정
        String uploadDir = new File("uploads/profile/").getAbsolutePath(); // ✅ 절대 경로 변환
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
            System.out.println("✅ 프로필 이미지 저장 폴더 생성됨: " + directory.getAbsolutePath());
        }

        // ✅ 📌 파일명: UUID + 원본 파일명
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File destinationFile = new File(uploadDir, filename);

        try {
            file.transferTo(destinationFile);
            System.out.println("✅ 파일 저장 완료: " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }

        // ✅ DB에 저장될 URL 경로 설정
        String profileImageUrl = "/uploads/profile/" + filename;
        user.setProfileImageUrl(profileImageUrl);
        userRepository.save(user);

        return profileImageUrl; // 프론트에서 사용할 URL 반환
    }

}
