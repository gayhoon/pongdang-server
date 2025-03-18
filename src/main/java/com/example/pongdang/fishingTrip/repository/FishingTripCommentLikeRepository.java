package com.example.pongdang.fishingTrip.repository;

import com.example.pongdang.fishingTrip.entity.FishingTripCommentEntity;
import com.example.pongdang.fishingTrip.entity.FishingTripCommentLikeEntity;
import com.example.pongdang.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FishingTripCommentLikeRepository extends JpaRepository<FishingTripCommentLikeEntity, Long> {

    int countByComment(FishingTripCommentEntity comment); // ✅ 특정 댓글의 좋아요 개수 조회
    Optional<FishingTripCommentLikeEntity> findByCommentAndUser(FishingTripCommentEntity comment, UserEntity user); // ✅ 특정 사용자가 특정 댓글에 좋아요 눌렀는지 확인
}
