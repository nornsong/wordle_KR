package com.koreanwordle.game.repository;

import com.koreanwordle.game.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    @Query("""
            select * from word order by rand() limit 1
    """)
    Optional<Word> findRandomWord();

    @Query("""
      select count(w) > 0
      from Word w
      where function('replace', w.word, '-', '') = :submittedWord
  """)
    boolean findWordInList(String submittedWord);
}
