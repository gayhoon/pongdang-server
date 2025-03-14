package com.example.pongdang.user.service;

import com.example.pongdang.user.dto.KakaoUserDto;
import com.example.pongdang.user.dto.UserDto;
import com.example.pongdang.user.entity.UserEntity;
import com.example.pongdang.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service // 이 클래스가 서비스 역할을 한다고 알려줌
@RequiredArgsConstructor // 자동으로 생성자를 만들어줌
public class UserService {

    private final UserRepository userRepository; // 데이터베이스를 다룰 수 있도록 연결

//    @Transactional // 데이터 저장 시 트랜잭션(안전하게 저장) 적용
//    public UserEntity saveUser(UserDto userDto) {
//
//        // 같은 이메일이 존재하는지 확인
//        Optional<UserEntity> existingUser = userRepository.findByEmail(userDto.getEmail());
//
//        // 이미 존재하는 사용자인 경우
//        if(existingUser.isPresent()){
//            // 사용자 정보 반환
//            return existingUser.get();
//        }
//
//        // 존재하지 않는 사용자인 경우 신규저장
//        UserEntity user = UserEntity.builder() // 빌더 패턴 사용
//                .email(userDto.getEmail())
//                .nickname(userDto.getNickname())
//                .build();
//
//        return userRepository.save(user); // 저장 후 `UserEntity` 반환
//    }

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
}
