package com.koreanwordle.game.service;

import com.koreanwordle.game.dto.GameSessionResponse;
import com.koreanwordle.game.dto.GuessResponse;

public interface GameSessionService {

    GameSessionResponse getCreateDailyGame(String userId);

    GameSessionResponse getCreateRandomGame(String userId);
    
    GuessResponse submitAnswer(String userId, Long sessionId, String submittedWord);
}
