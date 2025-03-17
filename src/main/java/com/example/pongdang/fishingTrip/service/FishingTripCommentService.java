package com.example.pongdang.fishingTrip.service;

import com.example.pongdang.fishingTrip.entity.FishingTripCommentEntity;
import com.example.pongdang.fishingTrip.entity.FishingTripEntity;
import com.example.pongdang.fishingTrip.repository.FishingTripCommentRepository;
import com.example.pongdang.fishingTrip.repository.FishingTripRepository;
import com.example.pongdang.fishingTrip.vo.ResponseFishingTripComment;
import com.example.pongdang.user.entity.UserEntity;
import com.example.pongdang.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FishingTripCommentService {

    private final FishingTripCommentRepository commentRepository;
    private final FishingTripRepository fishingTripRepository;
    private final UserRepository userRepository;

    // ✅ 댓글 작성
    @Transactional
    public FishingTripCommentEntity addComment(Long postId, String content, HttpServletRequest request) {
        String email = getEmailFromRequest(request);
        if (email == null) throw new RuntimeException("Unauthorized");

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FishingTripEntity post = fishingTripRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        FishingTripCommentEntity comment = FishingTripCommentEntity.builder()
                .fishingTrip(post)
                .user(user)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        return commentRepository.save(comment);
    }

    // ✅ 특정 게시글의 댓글 목록 조회
    public List<ResponseFishingTripComment> getComments(Long postId) {
        FishingTripEntity post = fishingTripRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        List<FishingTripCommentEntity> comments = commentRepository.findByFishingTripOrderByCreatedAtAsc(post);

        return comments.stream()
                .map(comment -> ResponseFishingTripComment.builder()
                        .id(comment.getId())
                        .authorNickname(comment.getUser().getNickname())
                        .authorProfileImage(comment.getUser().getProfileImageUrl())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .toList(); // Java 16 이상에서 사용 가능, Java 8~11에서는 `.collect(Collectors.toList())` 사용
    }


    // ✅ JWT에서 사용자 이메일 가져오기
    private String getEmailFromRequest(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization");
        if (jwtToken == null) return null;
        return jwtToken.replace("Bearer ", "");
    }
}
