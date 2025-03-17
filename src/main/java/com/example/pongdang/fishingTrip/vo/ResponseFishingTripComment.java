package com.example.pongdang.fishingTrip.vo;

import com.example.pongdang.fishingTrip.entity.FishingTripCommentEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ResponseFishingTripComment {
    private Long id;
    private String authorNickname;
    private String authorProfileImage;
    private String content;
    private LocalDateTime createdAt;
}