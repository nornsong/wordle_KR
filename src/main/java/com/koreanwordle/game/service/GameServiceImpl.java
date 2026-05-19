package com.koreanwordle.game.service;

import com.koreanwordle.game.domain.Game;
import com.koreanwordle.game.domain.Word;
import com.koreanwordle.game.repository.GameRepository;
import com.koreanwordle.game.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final WordRepository wordRepository;
    private final GameRepository gameRepository;

    @Override
    @Transactional
    public void getNewGame() {
        Word word = wordRepository.findRandomWord()
                .orElseThrow(() -> new IllegalStateException("등록된 단어가 없습니다."));

        gameRepository.save(new Game(word));
    }
}

