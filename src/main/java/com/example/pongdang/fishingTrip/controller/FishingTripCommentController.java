package com.example.pongdang.fishingTrip.controller;

import com.example.pongdang.fishingTrip.dto.FishingTripCommentDto;
import com.example.pongdang.fishingTrip.entity.FishingTripCommentEntity;
import com.example.pongdang.fishingTrip.service.FishingTripCommentService;
import com.example.pongdang.fishingTrip.vo.ResponseFishingTripComment;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fishingTrip/comments")
@RequiredArgsConstructor
public class FishingTripCommentController {

    private final FishingTripCommentService commentService;

    // 댓글 작성 API
    @PostMapping("/{commentId}")
    public ResponseEntity<ResponseFishingTripComment> addComment(
            @PathVariable("commentId") Long fishingTripId,  // @PathVariable은 받아온 URL에 있는 {postId}를 파라미터로 받아올 수 있음
            @RequestBody FishingTripCommentDto commentDto, // DTO 사용
            HttpServletRequest request) { // HttpServletRequest로 헤더, 쿠키, 토큰 등을 받아옴
        return ResponseEntity.ok(commentService.addComment(fishingTripId, commentDto.getContent(), request));
    }

    // 댓글 조회 API
    @GetMapping("/{commentId}")
    public ResponseEntity<List<ResponseFishingTripComment>> getComments(@PathVariable("commentId") Long fishingTripId, HttpServletRequest request) {
        return ResponseEntity.ok(commentService.getComments(fishingTripId, request));
    }

    // ✅ 댓글 좋아요 API
    @PostMapping("/{commentId}/like")
    public ResponseEntity<ResponseFishingTripComment> likeComment(@PathVariable("commentId") Long commentId, HttpServletRequest request) {

        return ResponseEntity.ok(commentService.toggleCommentLike(commentId, request));
    }

    // ✅ 댓글 좋아요 개수 조회 API
    @GetMapping("/{commentId}/likes")
    public ResponseEntity<Long> getCommentLikes(@PathVariable("commentId") Long commentId) {
        return ResponseEntity.ok(commentService.getCommentLikeCount(commentId));
    }
}
