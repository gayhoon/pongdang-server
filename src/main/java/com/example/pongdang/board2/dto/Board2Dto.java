package com.example.pongdang.board2.dto;

import com.example.pongdang.board2.entity.Board2;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board2Dto {
    private Long id;

    private String title;

    private String content;

    public Board2 toEntity(){
        return Board2.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }
}
