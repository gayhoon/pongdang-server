package com.example.pongdang.fishingTrip.service;

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
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class FishingTripService {

    private final FishingTripRepository fishingTripRepository;
    private final FishingTripImageRepository fishingTripImageRepository;
    private final FishingTripFishRepository fishingTripFishRepository;
    private final FileStorageService fileStorageService;

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
    public ResponseFishingTrip saveBoard(FishingTripDto fishingTripDto, List<MultipartFile> images, Map<String, MultipartFile> fishImages) {

        FishingTripEntity post;

        if (fishingTripDto.getId() == null) {
            post = FishingTripEntity.builder()
                    .cate(fishingTripDto.getCate())
                    .title(fishingTripDto.getTitle())
                    .location(fishingTripDto.getLocation())
                    .detail(fishingTripDto.getDetail())
                    .fishes(new ArrayList<>()) // ✅ 빈 리스트 추가 (null 방지)
                    .images(new ArrayList<>()) // ✅ 빈 리스트 추가 (null 방지)
                    .build();
            fishingTripRepository.save(post);
            fishingTripRepository.flush(); // JPA가 즉시 INSERT 실행하여 ID가 생성되도록 강제(신규글 작성 시 client가 바로 생성된 게시글의 id를 받을 수 있도록)
        } else {
            post = fishingTripRepository.findById(fishingTripDto.getId())
                    .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

            post.setCate(fishingTripDto.getCate());
            post.setTitle(fishingTripDto.getTitle());
            post.setLocation(fishingTripDto.getLocation());
            post.setDetail(fishingTripDto.getDetail());

            // fishes가 null이면 빈 리스트로 초기화
            if(post.getFishes() == null){
                post.setFishes(new ArrayList<>());
            }

            // images가 null이면 빈 리스트로 초기화
            if(post.getImages() == null){
                post.setImages(new ArrayList<>());
            }

            fishingTripRepository.save(post);
        }

        // ✅ 삭제할 이미지가 존재하면 DB에서 삭제
        if (fishingTripDto.getDeletedImages() != null && !fishingTripDto.getDeletedImages().isEmpty()) {
            System.out.println("🗑 삭제할 이미지 URL 목록: " + fishingTripDto.getDeletedImages());

            // ✅ DB에서 이미지 URL이 `deletedImages` 목록에 포함된 것만 삭제
            fishingTripImageRepository.deleteByFishingTripIdAndImageUrlIn(post.getId(), fishingTripDto.getDeletedImages());

            // ✅ 삭제 후 남아있는 이미지 목록 확인 (디버깅용)
            List<FishingTripImageEntity> remainingImages = fishingTripImageRepository.findByFishingTripId(post.getId());
            System.out.println("✅ 삭제 후 남은 이미지 개수: " + remainingImages.size());
        }

        // ✅ 새로운 이미지 추가
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

            // 기존 물고기 정보 삭제
            fishingTripFishRepository.deleteByFishingTripId(post.getId());

            // 새로운 물고기 정보 저
            List<FishingTripFishEntity> fishEntities = fishingTripDto.getFishes().stream().map(fish -> {

                int index = fishingTripDto.getFishes().indexOf(fish);
                String fishImageUrl = null;

                // fishImages에서 해당 index의 파일을 가져와 저장
                if (fishImages.containsKey("fishImages_" + index)) {
                    MultipartFile fishImageFile = fishImages.get("fishImages_" + index);
                    fishImageUrl = fileStorageService.saveFile(fishImageFile);
                } else {
                    fishImageUrl = fish.getImageUrl(); // 기존 URL 유지
                }

                return FishingTripFishEntity.builder()
                        .species(fish.getSpecies())
                        .size(fish.getSize())
                        .nickname(fish.getNickname())
                        .description(fish.getDescription())
                        .imageUrl(fishImageUrl) // ✅ 이미지 URL 저장
                        .fishingTrip(post) // ✅ 게시글과 연관관계 설정
                        .build();

            }).collect(Collectors.toList());

            fishingTripFishRepository.saveAll(fishEntities);
        }


        return ResponseFishingTrip.builder()
                .id(post.getId())
                .cate(post.getCate())
                .title(post.getTitle())
                .location(post.getLocation())
                .detail(post.getDetail())
                .date(post.getDate().toString())
                .images(post.getImages() != null
                        ? post.getImages().stream().map(FishingTripImageEntity::getImageUrl).collect(Collectors.toList())
                        : new ArrayList<>()) // ✅ images가 null일 경우 빈 리스트 반환
                .fishes(post.getFishes() != null
                        ? post.getFishes().stream().map(fish -> ResponseFishingTrip.FishingTripFishDto.builder()
                                .species(fish.getSpecies())
                                .size(fish.getSize())
                                .nickname(fish.getNickname())
                                .description(fish.getDescription())
                                .imageUrl(fish.getImageUrl())
                                .build())
                        .collect(Collectors.toList())
                        : new ArrayList<>()) // ✅ fishes가 null이면 빈 리스트 반환
                .build();
    }


    // ✅ 모든 게시글 조회
    public List<ResponseFishingTrip> getAllBoards() {
        List<FishingTripEntity> posts = fishingTripRepository.findAllByOrderByIdDesc();

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
                                .imageUrl(fish.getImageUrl())
                                .build())
                        .collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }

    // 특정 게시글 조회 기능
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
                .fishes(post.getFishes() != null
                        ? post.getFishes().stream().map(fish -> ResponseFishingTrip.FishingTripFishDto.builder()
                                .species(fish.getSpecies())
                                .size(fish.getSize())
                                .nickname(fish.getNickname())
                                .description(fish.getDescription())
                                .imageUrl(fish.getImageUrl())
                                .build())
                        .collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }

    // 특정 게시글 삭제 기능
    @Transactional
    public void deleteFishingTrip(Long id) {
        // ✅ 게시글 존재 여부 확인
        if (!fishingTripRepository.existsById(id)) {
            throw new RuntimeException("삭제할 게시글이 존재하지 않습니다.");
        }

        // ✅ 연관된 이미지 데이터 삭제
        fishingTripImageRepository.deleteByFishingTripId(id);

        // ✅ 연관된 물고기 데이터 삭제
        fishingTripFishRepository.deleteByFishingTripId(id);

        // ✅ 게시글 삭제
        fishingTripRepository.deleteById(id);

        System.out.println("✅ 게시글 삭제 완료: ID " + id);
    }
}
