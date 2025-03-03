package com.example.pongdang.fishingTrip.repository;

import com.example.pongdang.fishingTrip.entity.FishingTripImageEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FishingTripImageRepository extends JpaRepository<FishingTripImageEntity, Long> {
    List<FishingTripImageEntity> findByFishingTripId(Long fishingTripId); // ✅ 특정 게시글의 이미지 가져오기

    // ✅ 특정 게시글의 이미지 중 특정 URL 목록을 삭제하는 메서드 추가
    @Transactional
    void deleteByFishingTripIdAndImageUrlIn(Long fishingTripId, List<String> imageUrls);

    // 게시글 삭제에 따른 이미지 삭제
    void deleteByFishingTripId(Long fishingTripId);
}
