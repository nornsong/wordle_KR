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

    @Column(name = "word", nullable = false, unique = true)
    private String word;

    @Column(columnDefinition = "TEXT")
    private String definition;

    @Column  // 품사
    private String pos;

//    public word(String word, String definition, String pos) {
//        this.word = word;
//        this.definition = definition;
//        this.pos = pos;
//    }
}
