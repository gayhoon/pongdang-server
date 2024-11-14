package com.example.pongdang.board.controller;

import com.example.pongdang.board.dto.BoardDto;
import com.example.pongdang.board.entity.Board;
import com.example.pongdang.board.service.BoardService;
import com.example.pongdang.board.vo.ResponseBoard;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards") // 기본 경로 설정
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    /**
     * 게시글을 저장한다.
     * @param boardDto 저장할 게시글 정보
     * @return responseBoard 게시글 제목 및 내용
     */
    @PostMapping
    public ResponseBoard saveBoard(@RequestBody BoardDto boardDto) {

        Board board = boardService.saveBoard(boardDto);

        return ResponseBoard.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .build();
    }

    // 모든 게시글 조회
    @GetMapping
    public List<Board> getAllBoards() {
        return boardService.getAllBoards();
    }
}