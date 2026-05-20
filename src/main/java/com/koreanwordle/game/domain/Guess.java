package com.koreanwordle.game.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Guess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String guessWord;

    @Column // 정답제출일
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT") // 초성, 중성, 종성 힌트 json 저장
    private String answerResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

}
