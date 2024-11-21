package com.example.pongdang.board2.service;

import com.example.pongdang.board1.entity.Board1;
import com.example.pongdang.board2.dto.Board2Dto;
import com.example.pongdang.board2.entity.Board2;
import com.example.pongdang.board2.repository.Board2Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Board2Service {

    private final Board2Repository board2Repository;

    public Board2Service (Board2Repository board2Repository){
        this.board2Repository = board2Repository;
    }

    // 게시글 저장
    public Board2 saveBoard(Board2Dto board2Dto){

        Board2 saveBoard;

        // 신규등록
        if(board2Dto.getId() == null){
            Board2 board2 = board2Dto.toEntity();

            saveBoard = board2Repository.save(board2);
        }else{
            Optional<Board2> opBoard = board2Repository.findById(board2Dto.getId());
            Board2 findBoard = opBoard.orElseThrow();

            findBoard.setTitle(board2Dto.getTitle());
            findBoard.setContent(board2Dto.getContent());

            saveBoard = board2Repository.save(findBoard);
        }

        return saveBoard;
    };

    // 모든 게시글 조회
    public List<Board2> getAllBoards(){
        return board2Repository.findAll();
    };
}
