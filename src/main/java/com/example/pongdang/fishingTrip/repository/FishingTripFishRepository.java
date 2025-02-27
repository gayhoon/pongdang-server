package com.example.pongdang.fishingTrip.repository;

import com.example.pongdang.fishingTrip.entity.FishingTripFishEntity;
import com.example.pongdang.fishingTrip.entity.FishingTripImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FishingTripFishRepository extends JpaRepository<FishingTripFishEntity, Long> {
    List<FishingTripFishEntity> findByFishingTripId(Long fishingTripId);
}
