package com.example.pongdang.fishingTrip.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FishingTripEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cate;
    private String title;
    private String location;
    private String detail;

    @Column(updatable = false) // ✅ 작성일은 수정되지 않도록 설정
    private LocalDate date;

    @PrePersist
    protected void onCreate() {
        this.date = LocalDate.now(); // ✅ 자동으로 오늘 날짜 설정
    }

    @OneToMany(mappedBy = "fishingTrip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FishingTripFishEntity> fishes = new ArrayList<>();

    @OneToMany(mappedBy = "fishingTrip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FishingTripImageEntity> images = new ArrayList<>();

    private int viewCount = 0; // 조회수 증가 (기본값 0)

    //조회수 증가 메서드
    public void increaseViewCount(){
        this.viewCount += 1;
    }
}
