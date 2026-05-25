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

    @Column(unique = true)
    private LocalDate dailyGameDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameType gameType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id",  nullable = false)
    private Word word;

    public static Game dailyGame(Word word, LocalDate dailyGameDate) {
        Game game = new Game();
        game.word = word;
        game.dailyGameDate = dailyGameDate;
        game.gameType = GameType.DAILY;
        return game;
    }

    public static Game randomGame(Word word) {
        Game game = new Game();
        game.word = word;
        game.dailyGameDate = null;
        game.gameType = GameType.RANDOM;
        return game;
    }
}
