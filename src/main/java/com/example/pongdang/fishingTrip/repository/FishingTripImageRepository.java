package com.example.pongdang.fishingTrip.repository;

import com.example.pongdang.fishingTrip.entity.FishingTripImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FishingTripImageRepository extends JpaRepository<FishingTripImageEntity, Long> {
    List<FishingTripImageEntity> findByFishingTripId(Long fishingTripId); // ✅ 특정 게시글의 이미지 가져오기
}
