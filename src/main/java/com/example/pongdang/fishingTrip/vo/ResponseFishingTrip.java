package com.example.pongdang.fishingTrip.vo;

import com.example.pongdang.fishingTrip.dto.FishingTripFishDto;
import com.example.pongdang.fishingTrip.entity.FishingTripFishEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ResponseFishingTrip {
    private Long id;
    private String cate;
    private String title;
    private String location;
    private String detail;
    private String date;
    private int viewCount; // 조회수
    private List<String> images; // ✅ 여러 개의 이미지 URL을 저장
    private List<FishingTripFishDto> fishes; // 여러 마리 물고기 정보 포함

    @Getter
    @Builder
    public static class FishingTripFishDto{
        private String species;  // 물고기 종류
        private double size;     // 크기 (cm)
        private String nickname; // 별명
        private String description; // 설명
        private String imageUrl;
    }
}