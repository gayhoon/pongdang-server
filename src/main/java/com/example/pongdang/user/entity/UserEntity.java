package com.example.pongdang.user.entity;

import com.example.pongdang.fishingTrip.entity.FishingTripEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    // 🔹 회원이 작성한 게시글 (회원 삭제 시 함께 삭제됨)
    @OneToMany(
            mappedBy = "author",
            cascade = CascadeType.REMOVE, // 회원 삭제 시, 관련 게시글도 삭제됨.
            orphanRemoval = true) // 회원이 삭제되면 게시글이 자동으로 고아 객체가 되어 삭제됨.
    private List<FishingTripEntity> fishingTrips;
}
