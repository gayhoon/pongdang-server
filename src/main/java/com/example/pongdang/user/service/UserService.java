package com.example.pongdang.user.service;

import com.example.pongdang.user.dto.KakaoUserDto;
import com.example.pongdang.user.dto.UserDto;
import com.example.pongdang.user.entity.UserEntity;
import com.example.pongdang.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import com.example.pongdang.fishingTrip.repository.FishingTripRepository;

import java.util.Optional;

@Service // 이 클래스가 서비스 역할을 한다고 알려줌
@RequiredArgsConstructor // 자동으로 생성자를 만들어줌
public class UserService {

    private final UserRepository userRepository; // 데이터베이스를 다룰 수 있도록 연결
    private final FishingTripRepository fishingTripRepository; // 추가

    // 사용자 저장
    @Transactional // 데이터 저장 시 트랜잭션(안전하게 저장) 적용
    public UserEntity saveUser(KakaoUserDto kakaoUser) {

        return userRepository.findByEmail(kakaoUser.getEmail())
                .orElseGet(() -> userRepository.save((
                        UserEntity.builder()
                                .email(kakaoUser.getEmail())
                                .nickname(kakaoUser.getNickname())
                                .build()
                        )));
    }

    // 사용자 삭제
    @Transactional
    public void deleteUser(Long userId) {
        // 1️⃣ 회원 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 2️⃣ 회원이 작성한 모든 게시글 삭제
        fishingTripRepository.deleteByAuthor(user); // FishingTripRepository를 통해 게시글 삭제

        // 3️⃣ 회원 삭제
        userRepository.delete(user);
    }
}
