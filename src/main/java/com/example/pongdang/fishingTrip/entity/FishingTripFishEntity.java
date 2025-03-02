package com.example.pongdang.fishingTrip.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fishing_trip_fish")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FishingTripFishEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String species; // 물고기 종류

    private double size; // 물고기 사이즈

    private String nickname; // 물고기 별명

    private String description; // 물고기 내용

    private String imageUrl; // 물고기 사진 URL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fishing_trip_id")
    private FishingTripEntity fishingTrip;
}
