package com.example.pongdang.board.repository;

// 작성된 entity import
import com.example.pongdang.board.entity.Board;

// JpaRepository는 기본적으로 CRUD(생성, 조회, 수정, 삭제) 기능을 제공합니다.
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<엔티티, 기본 키 타입>을 상속하면 기본적인 CRUD 기능을 제공
// JpaRepository<Board, Long>에서 Board는 엔티티 클래스이고, Long은 엔티티의 기본 키 타입입니다.
public interface BoardRepository extends JpaRepository<Board, Long> {
}