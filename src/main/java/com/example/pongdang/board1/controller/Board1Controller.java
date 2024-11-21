package com.example.pongdang.board1.controller;

import com.example.pongdang.board1.dto.Board1Dto;
import com.example.pongdang.board1.entity.Board1;
import com.example.pongdang.board1.service.Board1Service;
import com.example.pongdang.board1.vo.ResponseBoard1;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards1")
public class Board1Controller {

    private final Board1Service board1Service;

    public Board1Controller(Board1Service board1Service){
        this.board1Service = board1Service;
    }

    @PostMapping
    public ResponseBoard1 saveBoard(@RequestBody Board1Dto board1Dto){

        Board1 board1 = board1Service.SaveBoard(board1Dto);

        return ResponseBoard1.builder()
                .title(board1.getTitle())
                .content(board1.getContent())
                .build();
    }

    // 모든 게시글 조회
    @GetMapping
    public List<Board1> getAllBoards(){
        return board1Service.gettAllBoards();
    }
}
