package com.koreanwordle.game.repository;

import com.koreanwordle.game.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    @Query(value =
            "select * from word order by rand() limit 1", nativeQuery = true)
    Optional<Word> findRandomWord();
}
