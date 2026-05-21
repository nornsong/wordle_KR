package com.koreanwordle.game.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(
    name = "game",
    uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_userId_gameDate",
                columnNames = {"userId", "gameDate"}
        )
    }
)
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column // uuid
    private String userId;

    @Column // 최대시도횟수
    private Integer maxAttempts;

    @Column // 누적시도횟수
    private Integer attemptsCount;

    @Column // null이면 랜덤단어문제, null이 아니면 오늘의 단어문제
    private LocalDate gameDate;

    @Column
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime completedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    private Word word;

    public Game(String userId, Word word) {
        this.userId = userId;
        this.word = word;
        this.maxAttempts = 6;
        this.attemptsCount = 0;
        this.startedAt = LocalDateTime.now();
        this.gameDate = null;
        this.status = GameStatus.IN_PROGRESS;
    }

    public static Game daily(String userId, Word word) {
        Game game = new Game(userId, word);
        game.gameDate = LocalDate.now();
        return game;
    }
}
