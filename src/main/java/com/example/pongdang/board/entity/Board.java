package com.example.pongdang.board.entity;

// 이 애노테이션을 클래스 위에 붙이면 해당 클래스가 JPA 엔티티임을 나타냅니다.
import jakarta.persistence.Entity;

// @Id로 지정된 기본 키 값이 자동으로 생성되도록 설정합니다.
import jakarta.persistence.GeneratedValue;

// 기본 키 값을 생성하는 방식(strategy)을 정의합니다. GenerationType.IDENTITY는 데이터베이스가 자동으로 키를 생성하도록 설정합니다.
import jakarta.persistence.GenerationType;

// 엔티티 클래스에서 데이터베이스의 **기본 키(primary key)**로 사용될 필드를 지정합니다.
import jakarta.persistence.Id;

// @Getter와 @Setter는 Board 클래스에 getter, setter 메서드를 자동으로 생성하여 코드의 중복을 줄여줍니다.
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// @NoArgsConstructor는 매개변수가 없는 기본 생성자를 자동으로 생성해줍니다.
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
}