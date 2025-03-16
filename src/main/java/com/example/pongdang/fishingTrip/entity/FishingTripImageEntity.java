package com.example.pongdang.fishingTrip.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fishing_trip_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FishingTripImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl; // 저장된 이미지 URL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fishing_trip_id", nullable = false)
    private FishingTripEntity fishingTrip;
}
