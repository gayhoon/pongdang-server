package com.example.pongdang.fishingTrip.dto;

import com.example.pongdang.board2.entity.Board2;
import com.example.pongdang.fishingTrip.entity.FishingTripEntity;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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
    private List<String> images; // ✅ 여러 개의 이미지 URL 저장

    public FishingTripEntity toEntity(){
        return FishingTripEntity.builder()
                .cate(this.cate)
                .title(this.title)
                .location(this.location)
                .detail(this.detail)
                .build();
    }
}
