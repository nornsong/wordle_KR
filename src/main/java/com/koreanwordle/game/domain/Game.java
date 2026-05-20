package com.koreanwordle.game.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private GameType gameType;

    @Column
    private String correctWord;

    @Column
    private String dailyWord;

    @Column // 최대시도횟수
    private Integer maxAttempts;

    @Column // 누적시도횟수
    private Integer attemptsCount;

    @Column // 오늘의 문제 확인용
    private LocalDate gameDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id")
    private Word word;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Guess> guesses = new ArrayList<>();

    public Game(Word word) {
        this.word = word;
        this.correctWord = word.getWord().replace("-", "");
        this.maxAttempts = 6;
        this.attemptsCount = 0;
        this.gameType = GameType.RANDOM;
    }

    public static Game daily(Word word) {
        Game game = new Game();
        game.word = word;
        game.dailyWord = word.getWord().replace("-", "");
        game.maxAttempts = 6;
        game.attemptsCount = 0;
        game.gameDate = LocalDate.now();
        game.gameType = GameType.DAILY;
        return game;
    }
    
    private String getAnswerWord(Game game) {
        if("DAILY".equals(game.getGameType())) {
            return game.getDailyWord();
        }
        return game.getCorrectWord();
    }
}
