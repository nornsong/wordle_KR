package com.koreanwordle.game.dto;

public record GuessRequest(
        Long gameId,
        String submittedWord
) { }
