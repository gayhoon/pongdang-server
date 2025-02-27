package com.example.pongdang.fishingTrip.service;

import com.example.pongdang.board.entity.Board;
import com.example.pongdang.fishingTrip.dto.FishingTripDto;
import com.example.pongdang.fishingTrip.entity.FishingTripEntity;
import com.example.pongdang.fishingTrip.entity.FishingTripFishEntity;
import com.example.pongdang.fishingTrip.entity.FishingTripImageEntity;
import com.example.pongdang.fishingTrip.repository.FishingTripFishRepository;
import com.example.pongdang.fishingTrip.repository.FishingTripRepository;
import com.example.pongdang.fishingTrip.repository.FishingTripImageRepository;
import com.example.pongdang.fishingTrip.vo.ResponseFishingTrip;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class FishingTripService {

    private final FishingTripRepository fishingTripRepository;
    private final FishingTripImageRepository fishingTripImageRepository;
    private final FishingTripFishRepository fishingTripFishRepository;
    private final FileStorageService fileStorageService; // ✅ 파일 저장 서비스 추가

    public FishingTripService(FishingTripRepository fishingTripRepository,
                              FishingTripImageRepository fishingTripImageRepository,
                              FishingTripFishRepository fishingTripFishRepository,
                              FileStorageService fileStorageService) {
        this.fishingTripRepository = fishingTripRepository;
        this.fishingTripImageRepository = fishingTripImageRepository;
        this.fishingTripFishRepository = fishingTripFishRepository;
        this.fileStorageService = fileStorageService;
    }

    // ✅ 게시글 저장 (신규 & 수정)
    @Transactional
    public ResponseFishingTrip saveBoard(FishingTripDto fishingTripDto, List<MultipartFile> images) {

        FishingTripEntity post;

        if (fishingTripDto.getId() == null) {
            post = FishingTripEntity.builder()
                    .cate(fishingTripDto.getCate())
                    .title(fishingTripDto.getTitle())
                    .location(fishingTripDto.getLocation())
                    .detail(fishingTripDto.getDetail())
                    .build();
            fishingTripRepository.save(post);
        } else {
            post = fishingTripRepository.findById(fishingTripDto.getId())
                    .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

            post.setCate(fishingTripDto.getCate());
            post.setTitle(fishingTripDto.getTitle());
            post.setLocation(fishingTripDto.getLocation());
            post.setDetail(fishingTripDto.getDetail());

            fishingTripRepository.save(post);
        }

        // ✅ 이미지가 존재할 경우만 이미지 저장
        if (images != null && !images.isEmpty()) {
            List<FishingTripImageEntity> imageEntities = new ArrayList<>();
            for (MultipartFile image : images) {
                String fileUrl = fileStorageService.saveFile(image); // ✅ 절대 경로 반환됨
                FishingTripImageEntity imageEntity = FishingTripImageEntity.builder()
                        .imageUrl(fileUrl)
                        .fishingTrip(post)
                        .build();
                imageEntities.add(imageEntity);
            }
            fishingTripImageRepository.saveAll(imageEntities);
        }

        // ✅ 물고기가 존재할 경우만 물고기 정보 저장
        if (fishingTripDto.getFishes() != null && !fishingTripDto.getFishes().isEmpty()) {
            List<FishingTripFishEntity> fishEntities = fishingTripDto.getFishes().stream().map(fish ->
                    FishingTripFishEntity.builder()
                            .species(fish.getSpecies())
                            .size(fish.getSize())
                            .nickname(fish.getNickname())
                            .description(fish.getDescription())
                            .fishingTrip(post) // ✅ 게시글과 연관관계 설정
                            .build()
            ).collect(Collectors.toList());

            fishingTripFishRepository.saveAll(fishEntities);
        }

        return ResponseFishingTrip.builder()
                .cate(post.getCate())
                .title(post.getTitle())
                .location(post.getLocation())
                .detail(post.getDetail())
                .date(post.getDate().toString())
                .images(post.getImages() != null
                        ? post.getImages().stream().map(FishingTripImageEntity::getImageUrl).collect(Collectors.toList())
                        : new ArrayList<>()) // ✅ images가 null일 경우 빈 리스트 반환
                .fishes(post.getFishes().stream()
                        .map(fish -> ResponseFishingTrip.FishingTripFishDto.builder()
                                .species(fish.getSpecies())
                                .size(fish.getSize())
                                .nickname(fish.getNickname())
                                .description(fish.getDescription())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }


    // ✅ 모든 게시글 조회
    public List<ResponseFishingTrip> getAllBoards() {
        List<FishingTripEntity> posts = fishingTripRepository.findAll();

        return posts.stream().map(post -> ResponseFishingTrip.builder()
                .id(post.getId())
                .cate(post.getCate())
                .title(post.getTitle())
                .detail(post.getDetail())
                .date(post.getDate().toString())
                .viewCount(post.getViewCount()) // 조회수
                .images(post.getImages().stream()
                        .map(FishingTripImageEntity::getImageUrl)
                        .collect(Collectors.toList()))
                .fishes(post.getFishes().stream()
                        .map(fish -> ResponseFishingTrip.FishingTripFishDto.builder()
                                .species(fish.getSpecies())
                                .size(fish.getSize())
                                .nickname(fish.getNickname())
                                .description(fish.getDescription())
                                .build())
                        .collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }

    // ✅ 특정 게시글 조회 기능 추가
    public ResponseFishingTrip getFishingTripById(Long id) {
        FishingTripEntity post = fishingTripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 조회수 증가
        post.increaseViewCount();
        fishingTripRepository.save(post); // 변경 사항 저장

        return ResponseFishingTrip.builder()
                .id(post.getId())
                .cate(post.getCate())
                .title(post.getTitle())
                .location(post.getLocation())
                .detail(post.getDetail())
                .date(post.getDate().toString())
                .viewCount(post.getViewCount()) // 조회수
                .images(post.getImages() != null
                        ? post.getImages().stream().map(FishingTripImageEntity::getImageUrl).collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }
}
