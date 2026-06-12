package com.koreanwordle.game.repository;

import com.koreanwordle.game.domain.Word;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    @Query("""
            select w
            from Word w
            where length(function('replace', w.word, '-', '')) = 3
            order by function('rand')
    """)
    List<Word> findRandomWord(Pageable pageable);

    @Query("""
      select count(w) > 0
      from Word w
      where function('replace', w.word, '-', '') = :submittedWord
  """)
    boolean findWordInList(String submittedWord);
}
