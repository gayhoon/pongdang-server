package com.example.pongdang.fishingTrip.dto;

import com.example.pongdang.fishingTrip.entity.FishingTripCommentEntity;
import com.example.pongdang.fishingTrip.entity.FishingTripFishEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FishingTripCommentDto {
    private String content; // 사용자가 입력한 댓글 내용

    public FishingTripCommentEntity toEntity(){
        return FishingTripCommentEntity.builder()
                .content(this.content)
                .build();
    }
}