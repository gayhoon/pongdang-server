package com.example.pongdang.fishingTrip.entity;

import com.example.pongdang.user.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FishingTripEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cate;
    private String title;
    private String location;
    private String detail;

    @Column(updatable = false) // 작성일은 수정되지 않도록 설정
    private LocalDate date;

    @PrePersist
    protected void onCreate() {
        this.date = LocalDate.now(); // 자동으로 오늘 날짜 설정
    }

    @OneToMany(mappedBy = "fishingTrip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<FishingTripFishEntity> fishes = new HashSet<>();

    @OneToMany(mappedBy = "fishingTrip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<FishingTripImageEntity> images = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY) // 작성자(userEntity와 연결)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    private int viewCount = 0; // 조회수 증가 (기본값 0 )

    @OneToMany(mappedBy = "fishingTrip", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<FishingTripCommentEntity> comments = new HashSet<>();

    //조회수 증가 메서드
    public void increaseViewCount(){
        this.viewCount += 1;
    }
}
