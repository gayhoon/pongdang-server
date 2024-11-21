package com.example.pongdang.board1.dto;

import com.example.pongdang.board1.entity.Board1;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board1Dto {

    private Long id;

    private String title;

    private String content;

    public Board1 toEntity(){
        return Board1.builder()
                .content(this.content)
                .title(this.title)
                .build();
    }
}
