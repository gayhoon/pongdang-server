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
    @Builder.Default // ✅ 기본 값 설정 (이전 경고 해결)
    private List<FishingTripImageEntity> images = new ArrayList<>();
}
