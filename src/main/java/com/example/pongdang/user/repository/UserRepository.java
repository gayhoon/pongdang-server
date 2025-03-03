package com.example.pongdang.user.repository;

import com.example.pongdang.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<User, Long>은 User 테이블을 다룰 수 있게 해줌
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // 닉네임이 이미 있는지 확인하는 메서드
    boolean existsByNickname(String nickname);
}
