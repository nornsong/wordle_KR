package com.koreanwordle.game.repository;

import com.koreanwordle.game.domain.Game;
import com.koreanwordle.game.domain.GameType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("""
            select g
            from Game g
            join fetch g.word
            where g.gameType = :gameType
                and g.dailyGameDate = :dailyGameDate
    """)
    Optional<Game> getDailyGame(GameType gameType, LocalDate dailyGameDate);

    @Query("""
            select trim(function('replace', w.word, '-', ''))
            from Game g
            join g.word w
            where g.id = :gameId
    """)
    Optional<String> findNormalizedAnswerWord(Long gameId);

    @Query("""
            select g
            from Game g
            where g.id = :gameId
    """)
    Optional<Game> findGameById(Long gameId);

    @Query("""
            select w.definition
            from Game g
            join g.word w
            where g.id = :gameId
    """)
    Optional<String> findAnswerDefinition(Long gameId);
}
