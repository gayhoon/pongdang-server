package com.example.pongdang.board.controller;

import com.example.pongdang.board.dto.BoardDto;
import com.example.pongdang.board.entity.Board;
import com.example.pongdang.board.service.BoardService;
import com.example.pongdang.board.vo.ResponseBoard;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 게시글 controller
 * - 게시글 상세조회 시 게시글을 작성한 사람인지 체크하는 로직 필요
 * - 게시글 삭제 시 게시글을 작성한 사람인지 체크하는 로직 필요
 */
@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private final ModelMapper modelMapper;

    /**
     * 게시글 전체 목록을 조회한다.
     * @return 게시글 목록
     */
    @GetMapping
    public ResponseEntity<List<ResponseBoard>> getAllBoards() {
        List<Board> boards = boardService.getAllBoards();

        List<ResponseBoard> responseBoards = Optional.ofNullable(boards)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(user -> modelMapper.map(user, ResponseBoard.class))
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(responseBoards);
    }

    /**
     * 게시글 상세정보를 조회한다.
     * @param id 게시글 PK
     * @return 게시글 상세정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseBoard> getBoardById(@PathVariable(value = "id") Long id) {
        Board board = boardService.getBoardById(id);

        if (board.getId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        ResponseBoard responseBoard = modelMapper.map(board, ResponseBoard.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseBoard);
    }

    /**
     * 게시글을 저장한다.
     * @param boardDto 저장할 게시글 정보
     * @return responseBoard 게시글 제목 및 내용
     */
    @PostMapping
    public ResponseEntity<ResponseBoard> saveBoard(@RequestBody BoardDto boardDto) {

        Board board = boardService.saveBoard(boardDto);

        ResponseBoard responseBoard = modelMapper.map(board, ResponseBoard.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseBoard);
    }

    /**
     * 게시글을 삭제한다.
     * @param id 게시글 PK
     * @return id 게시글 PK
     */
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseBoard> saveBoard(@PathVariable("id") Long id) {

        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        boardService.deleteBoard(id);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseBoard.builder()
                .id(id)
                .build());
    }

}