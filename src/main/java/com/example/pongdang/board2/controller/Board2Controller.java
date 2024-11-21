package com.example.pongdang.board2.controller;

import com.example.pongdang.board2.dto.Board2Dto;
import com.example.pongdang.board2.entity.Board2;
import com.example.pongdang.board2.service.Board2Service;
import com.example.pongdang.board2.vo.ResponseBoard2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards2")
public class Board2Controller {

    private final Board2Service board2Service;

    public Board2Controller(Board2Service board2Service){
        this.board2Service = board2Service;
    }

    // 게시글 저장
    @PostMapping
    public ResponseBoard2 saveBoard(@RequestBody Board2Dto board2Dto){

        Board2 board2 = board2Service.saveBoard(board2Dto);

        return ResponseBoard2.builder()
                .title(board2Dto.getTitle())
                .content(board2Dto.getContent())
                .build();
    };

    // 모든 게시글 조회
    @GetMapping
    public List<Board2> getAllBoards(){
        return board2Service.getAllBoards();
    };
}
