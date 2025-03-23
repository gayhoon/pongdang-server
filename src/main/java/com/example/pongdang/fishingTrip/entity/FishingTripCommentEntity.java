package com.example.pongdang.fishingTrip.entity;
import com.example.pongdang.user.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FishingTripCommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 댓글과 게시글 관계 (N:1)
    @JoinColumn(name = "fishing_trip_id", nullable = false)
    private FishingTripEntity fishingTrip;

    @ManyToOne(fetch = FetchType.LAZY) // 댓글과 사용자 관계 (N:1)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, length = 500) // 댓글 내용
    private String content;

    @Column(nullable = false) // 댓글 작성 시간
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true) // ✅ 댓글과 좋아요 관계 (1:N)
    private Set<FishingTripCommentLikeEntity> likes = new HashSet<>();
}
