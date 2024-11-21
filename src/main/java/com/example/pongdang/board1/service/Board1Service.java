package com.example.pongdang.board1.service;

import com.example.pongdang.board.entity.Board;
import com.example.pongdang.board1.dto.Board1Dto;
import com.example.pongdang.board1.entity.Board1;
import com.example.pongdang.board1.repository.Board1Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Board1Service {

    private final Board1Repository board1Repository;

    public Board1Service(Board1Repository board1Repository) {
        this.board1Repository = board1Repository;
    }

    // 게시글 저장
    public Board1 SaveBoard(Board1Dto board1Dto){
        Board1 saveBoard;

        // 신규등록
        if (board1Dto.getId() == null) {
            Board1 board1 = board1Dto.toEntity();

            saveBoard = board1Repository.save(board1);
        } else{
            Optional<Board1> opBoard = board1Repository.findById(board1Dto.getId());
            Board1 findBoard = opBoard.orElseThrow();

            findBoard.setTitle(board1Dto.getTitle());
            findBoard.setContent(board1Dto.getContent());

            saveBoard = board1Repository.save(findBoard);
        }

        return saveBoard;
    }

    // 모든 게시글 조회
    public List<Board1> gettAllBoards(){
        return board1Repository.findAll();
    }
}
