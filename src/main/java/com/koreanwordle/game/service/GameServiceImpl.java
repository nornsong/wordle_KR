package com.koreanwordle.game.service;

import com.koreanwordle.game.domain.Game;
import com.koreanwordle.game.domain.Word;
import com.koreanwordle.game.dto.GuessResponse;
import com.koreanwordle.game.repository.GameRepository;
import com.koreanwordle.game.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final WordRepository wordRepository;
    private final GameRepository gameRepository;

    @Override
    public String getDailyGame() {

        Game game = gameRepository.findFirstByDailyWordIsNotNullAndGameDate(LocalDate.now())
                .orElseThrow(() -> new IllegalStateException("오늘의 문제가 아직 생성되지 않았습니다."));

        return game.getDailyWord();
    }

    @Override
    @Transactional
    public void createDailyWord() {
        Word word = wordRepository.findRandomWord()
                .orElseThrow(() -> new IllegalStateException("등록된 단어가 없습니다."));

        gameRepository.save(Game.daily(word));
    }

    @Override
    @Transactional
    public void getCreateNewGame() {
        Word word = wordRepository.findRandomWord()
                .orElseThrow(() -> new IllegalStateException("등록된 단어가 없습니다."));

        gameRepository.save(new Game(word));
    }
    
    @Override
    @Transactional
    public GuessResponse submitAnswer(Long gameId, String submittedWord) {
        return null;
    }
}

