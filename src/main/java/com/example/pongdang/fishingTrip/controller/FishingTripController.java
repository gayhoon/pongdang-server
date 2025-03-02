package com.example.pongdang.fishingTrip.controller;

import com.example.pongdang.fishingTrip.dto.FishingTripDto;
import com.example.pongdang.fishingTrip.service.FishingTripService;
import com.example.pongdang.fishingTrip.vo.ResponseFishingTrip;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    // ✅ 게시글 저장 (신규 등록 & 수정) - JSON 데이터 + 이미지 처리
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}) // ✅ multipart/form-data 요청 허용
    public ResponseEntity<ResponseFishingTrip> saveBoard(
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestParam Map<String, MultipartFile> fishImages, // ✅ Map<String, MultipartFile>로 변경
            @RequestPart(value = "data") FishingTripDto fishingTripDto
    ) {

        return ResponseEntity.ok(fishingTripService.saveBoard(fishingTripDto, images, fishImages)); // ✅ fishImages 함께 전달
    }



    // ✅ 모든 게시글 조회 API
    @GetMapping
    public List<ResponseFishingTrip> getAllBoards() {
        return fishingTripService.getAllBoards();
    }

    // 특정 게시글 조회 API
    @GetMapping("/{id}")
    public ResponseEntity<ResponseFishingTrip> getFishingTripById(@PathVariable Long id) {
        ResponseFishingTrip post = fishingTripService.getFishingTripById(id);
        return ResponseEntity.ok(post);
    }

    // 특정 게시글 삭제 API
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFishingTrip(@PathVariable Long id){
        fishingTripService.deleteFishingTrip(id);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }
}
