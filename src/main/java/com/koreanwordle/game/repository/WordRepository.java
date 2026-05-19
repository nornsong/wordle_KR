package com.koreanwordle.game.repository;

import com.koreanwordle.game.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Integer> {
}
