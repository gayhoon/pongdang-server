package com.example.pongdang.fishingTrip.vo;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ResponseFishingTrip {
    private String cate;
    private String title;
    private String location;
    private String detail;
    private String date;
    private List<String> images; // ✅ 여러 개의 이미지 URL을 저장
}