package com.koreanwordle.game.service;

import com.koreanwordle.game.dto.GuessResponse;

public interface GameService {

    String getDailyGame();

    void createDailyWord();

    void getCreateNewGame();
    
    GuessResponse submitAnswer(Long gameId, String submittedWord);
}
