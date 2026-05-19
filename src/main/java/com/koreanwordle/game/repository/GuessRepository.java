package com.koreanwordle.game.repository;

import com.koreanwordle.game.domain.Guess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuessRepository extends JpaRepository<Guess, Long> {
}
