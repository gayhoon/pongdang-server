package com.example.pongdang.board.dto;

// 이 애노테이션을 클래스 위에 붙이면 해당 클래스가 JPA 엔티티임을 나타냅니다.

import com.example.pongdang.board.entity.Board;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {

    private Long id;

    private String title;

    private String content;

    public Board toEntity() {
        return Board.builder()
                .content(this.content)
                .title(this.title)
                .build();
    }
}