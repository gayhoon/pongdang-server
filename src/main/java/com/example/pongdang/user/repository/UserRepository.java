package com.example.pongdang.user.repository;

import com.example.pongdang.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// JpaRepository<User, Long>은 User 테이블을 다룰 수 있게 해줌
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // 이메일로 존재 여부 확인
    boolean existsByEmail(String email);

    // 이메일로 사용자 찾기
    Optional<UserEntity> findByEmail(String email);
}
