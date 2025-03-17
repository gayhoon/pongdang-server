package com.example.pongdang.fishingTrip.repository;

import com.example.pongdang.fishingTrip.entity.FishingTripCommentEntity;
import com.example.pongdang.fishingTrip.entity.FishingTripEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FishingTripCommentRepository extends JpaRepository<FishingTripCommentEntity, Long> {
    List<FishingTripCommentEntity> findByFishingTripOrderByCreatedAtAsc(FishingTripEntity fishingTrip);
}