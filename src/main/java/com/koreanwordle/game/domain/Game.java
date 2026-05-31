package com.koreanwordle.game.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column // 누적시도횟수
    private Integer attemptsCount;

    @Column // 최대시도횟수
    private Integer maxAttemptsCount;

    @Column // 풀이 시작 시간
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime completedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status;

    @Column(unique = true) // 오늘의 단어 문제용 시간 필드
    private LocalDate dailyGameDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameType gameType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id",  nullable = false)
    private Word word;

    public static Game createDailyGame(Word word, LocalDate dailyGameDate) {
        Game game = new Game();
        game.word = word;
        game.attemptsCount = 0;
        game.maxAttemptsCount = 7;
        game.startedAt = LocalDateTime.now();
        game.completedAt = null;
        game.dailyGameDate = dailyGameDate;
        game.gameType = GameType.DAILY;
        game.status = GameStatus.IN_PROGRESS;
        return game;
    }

    public static Game createRandomGame(Word word) {
        Game game = new Game();
        game.word = word;
        game.attemptsCount = 0;
        game.maxAttemptsCount = 7;
        game.startedAt = LocalDateTime.now();
        game.completedAt = null;
        game.dailyGameDate = null;
        game.gameType = GameType.RANDOM;
        game.status = GameStatus.IN_PROGRESS;
        return game;
    }
}
