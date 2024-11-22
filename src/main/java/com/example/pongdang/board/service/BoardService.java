package com.example.pongdang.board.service;

import com.example.pongdang.board.dto.BoardDto;
import com.example.pongdang.board.entity.Board;
import com.example.pongdang.board.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    /**
     * 게시글 전체 목록을 조회한다.
     * @return 게시글 목록
     */
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    /**
     * 게시글 상세정보를 조회한다.
     * @param id 게시글 PK
     * @return 게시글 상세정보
     */
    public Board getBoardById(Long id) {
        Optional<Board> opBoard = boardRepository.findById(id);

        return opBoard.orElse(Board.builder().build());
    }

    /**
     * 게시글을 저장한다.
     * @param boardDto 저장할 게시글 정보
     * @return responseBoard 게시글 제목 및 내용
     */
    public Board saveBoard(BoardDto boardDto) {

        Board saveBoard;

        // 신규 등록
        if(boardDto.getId() == null) {
            Board board = boardDto.toEntity();

            saveBoard = boardRepository.save(board);
        } else {
            /*
            Optional NULL 처리에 대한 문제를 해결하기 위해 jdk8버전에 도입되었다.
            NULL인지 아닌지 몰라도 일단 감싸서 NULL_POINTER_EXCEPTION이 발생하지 않게 도와준다.
             */
            Optional<Board> opBoard = boardRepository.findById(boardDto.getId());
            Board findBoard = opBoard.orElseThrow();

            findBoard.setTitle(boardDto.getTitle());
            findBoard.setContent(boardDto.getContent());

            saveBoard = boardRepository.save(findBoard);
        }

        return saveBoard;
    }

    /**
     * 게시글을 삭제한다.
     * @param id 게시글 PK
     */
    public void deleteBoard(Long id) {

        Optional<Board> opBoard = boardRepository.findById(id);
        Board board = opBoard.orElseThrow();

        boardRepository.delete(board);
    }

}