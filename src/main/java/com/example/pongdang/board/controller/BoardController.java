package com.example.pongdang.board.controller;

import com.example.pongdang.board.entity.Board;
import com.example.pongdang.board.service.BoardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards") // 기본 경로 설정
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 게시글 생성
    @PostMapping
    public Board createBoard(@RequestBody Board board) {
        return boardService.saveBoard(board);
    }

    // 모든 게시글 조회
    @GetMapping
    public List<Board> getAllBoards() {
        return boardService.getAllBoards();
    }
}