package com.example.pongdang.fishingTrip.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FishingTripCommentDto {
    private String content; // 사용자가 입력한 댓글 내용
}