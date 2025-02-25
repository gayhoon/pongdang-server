package com.example.pongdang.fishingTrip.controller;

import com.example.pongdang.fishingTrip.dto.FishingTripDto;
import com.example.pongdang.fishingTrip.service.FishingTripService;
import com.example.pongdang.fishingTrip.vo.ResponseFishingTrip;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fishingTrip")
public class FishingTripController {

    private final FishingTripService fishingTripService;

    public FishingTripController(FishingTripService fishingTripService) {
        this.fishingTripService = fishingTripService;
    }

    // ✅ 게시글 저장 (신규 등록 & 수정) - JSON 데이터 + 이미지 처리
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}) // ✅ multipart/form-data 요청 허용
    public ResponseEntity<ResponseFishingTrip> saveBoard(
            @ModelAttribute FishingTripDto fishingTripDto, // ✅ JSON 데이터 받기 (@RequestPart 대신 사용 가능)
            @RequestPart(value = "images", required = false) List<MultipartFile> images // ✅ 이미지 파일 선택적
    ) {
        return ResponseEntity.ok(fishingTripService.saveBoard(fishingTripDto, images));
    }

    // ✅ 모든 게시글 조회
    @GetMapping
    public List<ResponseFishingTrip> getAllBoards() {
        return fishingTripService.getAllBoards();
    }
}
