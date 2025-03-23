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
    private int likeCount;
    private boolean isLiked;

    // ✅ 여기에 넣어야 함!
    public static ResponseFishingTripComment of(FishingTripCommentEntity comment, boolean isLiked) {
        return ResponseFishingTripComment.builder()
                .id(comment.getId())
                .authorNickname(comment.getUser().getNickname())
                .authorProfileImage(comment.getUser().getProfileImageUrl())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .likeCount(comment.getLikes() != null ? comment.getLikes().size() : 0)
                .isLiked(isLiked)
                .build();
    }
}