package com.koreanwordle.game.repository;

import com.koreanwordle.game.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {

    Optional<Game> findFirstByDailyWordIsNotNullAndGameDate(LocalDate dailyWord);
}
