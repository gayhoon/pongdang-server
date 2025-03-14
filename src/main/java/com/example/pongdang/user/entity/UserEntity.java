package com.example.pongdang.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 포함한 생성자 자동 생성
@Builder
public class UserEntity {

    @Id // 이 필드를 기본 키(Primary Key)로 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 (1, 2, 3...)
    private Long id;

    private String email;

    private String nickname;
}
