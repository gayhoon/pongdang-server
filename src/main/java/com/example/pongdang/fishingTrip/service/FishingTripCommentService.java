package com.example.pongdang.fishingTrip.service;

import com.example.pongdang.fishingTrip.entity.FishingTripCommentEntity;
import com.example.pongdang.fishingTrip.entity.FishingTripCommentLikeEntity;
import com.example.pongdang.fishingTrip.entity.FishingTripEntity;
import com.example.pongdang.fishingTrip.repository.FishingTripCommentLikeRepository;
import com.example.pongdang.fishingTrip.repository.FishingTripCommentRepository;
import com.example.pongdang.fishingTrip.repository.FishingTripRepository;
import com.example.pongdang.fishingTrip.vo.ResponseFishingTripComment;
import com.example.pongdang.user.entity.UserEntity;
import com.example.pongdang.user.provider.JwtTokenProvider;
import com.example.pongdang.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FishingTripCommentService {

    private final FishingTripCommentRepository commentRepository;
    private final FishingTripRepository fishingTripRepository;
    private final UserRepository userRepository;
    private final FishingTripCommentLikeRepository likeRepository;
    private final JwtTokenProvider jwtProvider;

    // ✅ 댓글 작성
    @Transactional
    public ResponseFishingTripComment addComment(Long fishingTripId, String content, HttpServletRequest request) {
        String email = jwtProvider.getEmailFromRequest(request);
        if (email == null) throw new RuntimeException("Unauthorized");

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FishingTripEntity post = fishingTripRepository.findById(fishingTripId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        FishingTripCommentEntity comment = FishingTripCommentEntity.builder()
                .fishingTrip(post)
                .user(user)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        FishingTripCommentEntity saved = commentRepository.save(comment);

        boolean isLiked = false; // 작성 직후니까 false
        return ResponseFishingTripComment.of(saved, isLiked);
    }

    // ✅ 특정 게시글의 댓글 목록 조회 (좋아요 포함)
    @Transactional(readOnly = true)
    public List<ResponseFishingTripComment> getComments(Long postId, HttpServletRequest request) {
        String email = jwtProvider.getEmailFromRequest(request);

        // ✅ 게시글 존재 여부 확인
        var post = fishingTripRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // ✅ 해당 게시글의 댓글 목록 조회
        List<FishingTripCommentEntity> comments = commentRepository.findByFishingTripOrderByCreatedAtAsc(post);
        return comments.stream()
                .map(comment -> {
                    boolean isLiked = false;
                    int likeCount = likeRepository.countByComment(comment);

                    System.out.println("🟢 댓글 ID: " + comment.getId() + ", 좋아요 개수: " + likeCount);

                    // ✅ 로그인한 사용자가 있을 경우, 해당 댓글에 좋아요를 눌렀는지 확인
                    if (email != null) {
                        UserEntity user = userRepository.findByEmail(email).orElse(null);
                        if (user != null) {
                            isLiked = likeRepository.findByCommentAndUser(comment, user).isPresent();
                        }
                    }

                    return ResponseFishingTripComment.builder()
                            .id(comment.getId())
                            .authorNickname(comment.getUser().getNickname())
                            .authorProfileImage(comment.getUser().getProfileImageUrl())
                            .content(comment.getContent())
                            .createdAt(comment.getCreatedAt())
                            .likeCount(likeCount)
                            .isLiked(isLiked)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // ✅ 댓글 좋아요 기능
    @Transactional
    public void toggleCommentLike(Long commentId, HttpServletRequest request) {
        String email = jwtProvider.getEmailFromRequest(request);
        if (email == null) throw new RuntimeException("Unauthorized");

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FishingTripCommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        Optional<FishingTripCommentLikeEntity> existingLike = likeRepository.findByCommentAndUser(comment, user);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get()); // ✅ 이미 좋아요 눌렀다면 삭제
        } else {
            likeRepository.save(FishingTripCommentLikeEntity.builder() // ✅ 좋아요 추가
                    .comment(comment)
                    .user(user)
                    .build());
        }

        // ✅ 즉시 반영
        likeRepository.flush();
    }

    // ✅ 특정 댓글의 좋아요 개수 반환
    @Transactional(readOnly = true)
    public long getCommentLikeCount(Long commentId) {
        FishingTripCommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        return likeRepository.countByComment(comment);
    }
}
