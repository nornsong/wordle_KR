package com.koreanwordle.game.repository;

import com.koreanwordle.game.domain.Game;
import com.koreanwordle.game.domain.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {



    @Query("""
            select gs
            from GameSession gs
            join fetch gs.game g
            join fetch g.word
            where gs.userId = :userId
                and gs.game = :game
    """)
    Optional<GameSession> findDailyGame(String userId, Game game);

    @Query("""
            select gs
            from GameSession gs
            join fetch gs.game g
            join fetch g.word
            where gs.userId = :userId
                and gs.game = :game
    """)
    Optional<GameSession> findRandomGame(String userId, Game game);

    @Query(value = """
            select replace(w.word, '-', '')
            from game_session gs
            join game g on gs.name_id = g.id
            join word w on g.word_id = w.id
            where gs.userId = :userId
                and gs.id = :sessionId
    """, nativeQuery = true)
    Optional<String> findSessionAndGame(String userId, Long sessionId);
}
