package com.example.pongdang.fishingTrip.dto;

import com.example.pongdang.fishingTrip.entity.FishingTripEntity;
import com.example.pongdang.fishingTrip.entity.FishingTripFishEntity;
import com.example.pongdang.user.entity.UserEntity;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FishingTripDto {
    private Long id;
    private String cate;
    private String title;
    private String location;
    private String detail;
    private String authorEmail; // 작성자 이메일 추가
    private List<String> images; // 여러 개의 이미지 URL 저장
    private List<FishingTripFishDto> fishes; // 여러 마리 물고기 정보 포함
    private List<String> existingImages; // 유지할 기존 이미지 목록
    private List<String> deletedImages;  // 삭제할 이미지 목록 추가

    public FishingTripEntity toEntity(UserEntity author){ // UserEntity author 추가
        FishingTripEntity fishingTripEntity = FishingTripEntity.builder()
                .id(this.id)
                .cate(this.cate)
                .title(this.title)
                .location(this.location)
                .detail(this.detail)
                .author(author) // UserEntity를 저장해야 함!
                .build();

        // 물고기 정보가 있을 경우 변환하여 추가
        if (this.fishes != null) {
            List<FishingTripFishEntity> fishEntities = this.fishes.stream()
                    .map(FishingTripFishDto::toEntity)
                    .peek(fish -> fish.setFishingTrip(fishingTripEntity)) // 조행기와 연관 설정
                    .toList();
            fishingTripEntity.setFishes(new HashSet<>(fishEntities));
        }

        return fishingTripEntity;
    }
}
