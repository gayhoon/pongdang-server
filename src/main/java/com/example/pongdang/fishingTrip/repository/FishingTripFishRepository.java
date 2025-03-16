package com.example.pongdang.fishingTrip.repository;

import com.example.pongdang.fishingTrip.entity.FishingTripEntity;
import com.example.pongdang.fishingTrip.entity.FishingTripFishEntity;
import com.example.pongdang.fishingTrip.entity.FishingTripImageEntity;
import com.example.pongdang.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FishingTripFishRepository extends JpaRepository<FishingTripFishEntity, Long> {
    List<FishingTripFishEntity> findByFishingTripId(Long fishingTripId);

    @Transactional
    @Modifying
    @Query("DELETE FROM FishingTripFishEntity f WHERE f.fishingTrip.id = :fishingTripId")
    void deleteByFishingTripId(@Param("fishingTripId") Long fishingTripId);// 특정 사용자가 작성한 모든 게시글 삭제
}
