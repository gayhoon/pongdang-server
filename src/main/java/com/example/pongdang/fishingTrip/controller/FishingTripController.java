package com.example.pongdang.fishingTrip.controller;

import com.example.pongdang.fishingTrip.dto.FishingTripDto;
import com.example.pongdang.fishingTrip.service.FishingTripService;
import com.example.pongdang.fishingTrip.vo.ResponseFishingTrip;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.pongdang.user.provider.JwtTokenProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/fishingTrip")
@RequiredArgsConstructor
public class FishingTripController {

    private final FishingTripService fishingTripService;
    private final JwtTokenProvider jwtProvider; // 추가 (JwtTokenProvider 주입)

    // 게시글 저장 (신규 등록 & 수정) - JSON 데이터 + 이미지 처리
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}) // multipart/form-data 요청 허용
    public ResponseEntity<ResponseFishingTrip> saveBoard(
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestParam Map<String, MultipartFile> fishImages, // Map<String, MultipartFile>로 변경
            @RequestPart(value = "data") FishingTripDto fishingTripDto,
            HttpServletRequest request // HttpServletRequest에서 JWT 쿠키 가져오기
    ) {

        // JWT 쿠키 가져오기
        String jwtToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }

        // JWT 검증
        if (jwtToken == null || !jwtProvider.validateToken(jwtToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(fishingTripService.saveBoard(fishingTripDto, images, fishImages, jwtToken));
    }



    // 모든 게시글 조회 API
    @GetMapping
    public List<ResponseFishingTrip> getAllBoards() {
        return fishingTripService.getAllBoards();
    }

    // 특정 게시글 조회 API
    @GetMapping("/{id}")
    public ResponseEntity<ResponseFishingTrip> getFishingTripById(
            @PathVariable("id") Long id,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader // ✅ JWT 토큰 추가
    ) {
        // ✅ Authorization 헤더에서 Bearer 토큰 추출 (토큰이 없을 경우 대비)
        String jwtToken = (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
                ? authorizationHeader.substring(7).trim()
                : null;

        try {
            ResponseFishingTrip post = fishingTripService.getFishingTripById(id, jwtToken);
            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // ✅ JWT 문제 시 401 응답
        }
    }

    // 특정 게시글 삭제 API
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFishingTrip(@PathVariable("id") Long id){
        fishingTripService.deleteFishingTrip(id);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }
}
