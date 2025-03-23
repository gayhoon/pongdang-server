package com.example.pongdang.fishingTrip.repository;

import com.example.pongdang.fishingTrip.entity.FishingTripEntity;
import com.example.pongdang.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // ✅ 추가!

import java.util.List;

public interface FishingTripRepository extends JpaRepository<FishingTripEntity, Long> {
    // 기본메서드 + 최신순 정렬
    List<FishingTripEntity> findAllByOrderByIdDesc();

    void deleteByAuthor(UserEntity author);

    // ✅ [추가] Lazy 로딩 방지용 fetch join 쿼리
    @Query("""
        SELECT DISTINCT f FROM FishingTripEntity f
        LEFT JOIN FETCH f.author
        LEFT JOIN FETCH f.images
        LEFT JOIN FETCH f.fishes
        LEFT JOIN FETCH f.comments c
        LEFT JOIN FETCH c.user
        ORDER BY f.id DESC
    """)
    List<FishingTripEntity> findAllWithAllRelations();
}
