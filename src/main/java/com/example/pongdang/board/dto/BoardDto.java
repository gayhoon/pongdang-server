package com.example.pongdang.board.dto;

import com.example.pongdang.board.entity.Board;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {

    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    public Board toEntity() {
        return Board.builder()
                .content(this.content)
                .title(this.title)
                .build();
    }
}