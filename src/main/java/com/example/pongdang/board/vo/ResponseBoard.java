package com.example.pongdang.board.vo;

// 이 애노테이션을 클래스 위에 붙이면 해당 클래스가 JPA 엔티티임을 나타냅니다.

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseBoard {

    private Long id;

    private String title;

    private String content;

}