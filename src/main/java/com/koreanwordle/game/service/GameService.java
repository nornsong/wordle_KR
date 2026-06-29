package com.koreanwordle.game.service;

import com.koreanwordle.game.dto.GameResponse;
import com.koreanwordle.game.dto.GuessResponse;

public interface GameService {

    GameResponse getCreateDailyGame();

    GameResponse getCreateRandomGame();
    
    GuessResponse submitAnswer(Long gameId, String submittedWord, Integer attemptNumber);
}
