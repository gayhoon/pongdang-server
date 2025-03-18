package com.example.pongdang.fishingTrip.entity;

import com.example.pongdang.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "fishing_trip_comment_like",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"comment_id", "user_id"})}) // ✅ 중복 방지
public class FishingTripCommentLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // ✅ 댓글과 좋아요 관계 (N:1)
    @JoinColumn(name = "comment_id", nullable = false)
    private FishingTripCommentEntity comment;

    @ManyToOne(fetch = FetchType.LAZY) // ✅ 사용자와 좋아요 관계 (N:1)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
