package com.example.pongdang.fishingTrip.service;

import com.example.pongdang.fishingTrip.dto.FishingTripDto;
import com.example.pongdang.fishingTrip.entity.FishingTripEntity;
import com.example.pongdang.fishingTrip.entity.FishingTripImageEntity;
import com.example.pongdang.fishingTrip.repository.FishingTripRepository;
import com.example.pongdang.fishingTrip.repository.FishingTripImageRepository;
import com.example.pongdang.fishingTrip.vo.ResponseFishingTrip;
import org.springframework.stereotype.Service;
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

    public FishingTripService(FishingTripRepository fishingTripRepository, FishingTripImageRepository fishingTripImageRepository) {
        this.fishingTripRepository = fishingTripRepository;
        this.fishingTripImageRepository = fishingTripImageRepository;
    }

    // ✅ 게시글 저장 (신규 & 수정)
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

        // ✅ 이미지가 존재할 경우만 저장하도록 수정
        if (images != null && !images.isEmpty()) {
            List<FishingTripImageEntity> imageEntities = new ArrayList<>();
            for (MultipartFile image : images) {
                String imageUrl = "https://example.com/" + image.getOriginalFilename(); // ✅ 실제 저장 경로 적용
                FishingTripImageEntity imageEntity = FishingTripImageEntity.builder()
                        .imageUrl(imageUrl)
                        .fishingTrip(post)
                        .build();
                imageEntities.add(imageEntity);
            }
            fishingTripImageRepository.saveAll(imageEntities);
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
                .build();
    }


    // ✅ 모든 게시글 조회
    public List<ResponseFishingTrip> getAllBoards() {
        List<FishingTripEntity> posts = fishingTripRepository.findAll();

        return posts.stream().map(post -> ResponseFishingTrip.builder()
                .cate(post.getCate())
                .title(post.getTitle())
                .detail(post.getDetail())
                .date(post.getDate().toString())
                .images(post.getImages().stream().map(FishingTripImageEntity::getImageUrl).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }
}
