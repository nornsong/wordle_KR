package com.koreanwordle.game.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String correctWord;

    @Column // 최대시도횟수
    private Integer maxAttempts;

    @Column // 누적시도횟수
    private Integer attemptsCount;

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
    }
}
