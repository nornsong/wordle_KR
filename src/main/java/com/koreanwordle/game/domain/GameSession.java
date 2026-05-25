package com.koreanwordle.game.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Entity
@Getter
@NoArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_game",
                        columnNames = {"userId", "game_id"}
                )
        }
)
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // uuid
    private String userId;

    @Column // 누적시도횟수
    private Integer attemptsCount;

    @Column // 최대시도횟수
    private Integer maxAttemptsCount;

    @Column
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime completedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    public GameSession(String userId, Game game) {
        this.userId = userId;
        this.game = game;
        this.attemptsCount = 0;
        this.maxAttemptsCount = 7;
        this.status = GameStatus.IN_PROGRESS;
        this.startedAt = now();
        this.completedAt = null;
    }
}
