package com.koreanwordle.game.repository;

import com.koreanwordle.game.domain.Game;
import com.koreanwordle.game.domain.GameType;
import com.koreanwordle.game.dto.GameSessionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("""
            select g
            from Game g
            join fetch g.word
            where g.gameType = :gameType
                and g.dailyGameDate = :dailyGameDate""")
    Optional<Game> getDailyGame(GameType gameType, LocalDate dailyGameDate);

    @Query("""
            select g
            from Game g
            join fetch g.word
            where g.gameType = :gameType
                and g.""")
    Optional<Game> getRandomGame(GameType gameType);
}
