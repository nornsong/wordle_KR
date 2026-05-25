package com.koreanwordle.game.dto;

public record GuessRequest(
        String userId,
        Long sessionId,
        String submittedWord
) { }
