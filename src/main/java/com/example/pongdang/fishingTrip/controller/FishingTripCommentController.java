package com.example.pongdang.fishingTrip.controller;

import com.example.pongdang.fishingTrip.dto.FishingTripCommentDto;
import com.example.pongdang.fishingTrip.entity.FishingTripCommentEntity;
import com.example.pongdang.fishingTrip.service.FishingTripCommentService;
import com.example.pongdang.fishingTrip.vo.ResponseFishingTripComment;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.debug.DebugFilter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fishingTrip")
@RequiredArgsConstructor
public class FishingTripCommentController {

    private final FishingTripCommentService commentService;

    // ✅ 댓글 작성 API
    @PostMapping("/{postId}/comments")
    public ResponseEntity<FishingTripCommentEntity> addComment(
            @PathVariable("postId") Long postId,  // URL 경로 변수
            @RequestBody FishingTripCommentDto commentDto, // ✅ DTO 사용
            HttpServletRequest request) {
        return ResponseEntity.ok(commentService.addComment(postId, commentDto.getContent(), request));
    }

    // ✅ 댓글 조회 API
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<ResponseFishingTripComment>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getComments(postId));
    }

}
