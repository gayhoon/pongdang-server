package com.example.pongdang.user.service;

import com.example.pongdang.user.entity.UserEntity;
import com.example.pongdang.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service // 이 클래스가 서비스 역할을 한다고 알려줌
@RequiredArgsConstructor // 자동으로 생성자를 만들어줌
public class UserService {

    private final UserRepository userRepository; // 데이터베이스를 다룰 수 있도록 연결

    @Transactional // 데이터 저장 시 트랜잭션(안전하게 저장) 적용
    public void saveUser(String nickname) {
        // 같은 닉네임이 존재하는지 확인
        if (!userRepository.existsByNickname(nickname)) {
            UserEntity user = new UserEntity(); // 새 유저 객체 만들기
            user.setNickname(nickname); // 닉네임 설정
            userRepository.save(user); // 데이터베이스에 저장
        }
    }
}
