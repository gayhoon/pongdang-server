package com.example.pongdang.fishingTrip.dto;

import com.example.pongdang.fishingTrip.entity.FishingTripEntity;
import com.example.pongdang.fishingTrip.entity.FishingTripFishEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FishingTripFishDto {
    private Long id;
    private String species;
    private double size;
    private String nickname;
    private String description;
    private String imageUrl;

    public FishingTripFishEntity toEntity() {
        return FishingTripFishEntity.builder()
                .species(this.species)
                .size(this.size)
                .nickname(this.nickname)
                .description(this.description)
                .imageUrl(this.imageUrl)
                .build();
    }
}
