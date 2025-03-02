package com.example.pongdang.fishingTrip.repository;

import com.example.pongdang.fishingTrip.entity.FishingTripEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FishingTripRepository extends JpaRepository<FishingTripEntity, Long> {
    // 기본메서드 + 최신순 정렬
    List<FishingTripEntity> findAllByOrderByIdDesc();
}
