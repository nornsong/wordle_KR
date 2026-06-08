package com.koreanwordle.game.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "word")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 동음이의어로 인한 unique 제약 삭제
    @Column(name = "word", nullable = false)
    private String word;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String definition;

    // 동음이의어 구분을 위한 어깨번호
    @Column
    private Integer homonymNum;

    @Column  // 품사
    private String pos;
}
