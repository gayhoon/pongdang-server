package com.example.pongdang.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 포함한 생성자 자동 생성
public class UserEntity {

    @Id // 이 필드를 기본 키(Primary Key)로 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 (1, 2, 3...)
    private Long id;

    @Column(nullable = true, unique = true) // 닉네임은 반드시 있어야 하고 중복 불가
    private String nickname;
}
