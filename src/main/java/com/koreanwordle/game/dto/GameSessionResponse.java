package com.koreanwordle.game.dto;

import java.time.LocalDateTime;

public record GameSessionResponse(
        Long sessionId,
        Long gameId,
        Integer attemptsCount,
        Integer maxAttemptsCount,
        LocalDateTime startedAt,
        String status
) {
    public static GameSessionResponse of(
            Long sessionId,
            Long gameId,
            Integer attemptsCount,
            Integer maxAttemptsCount,
            LocalDateTime startedAt,
            String status
    ) {
        return new GameSessionResponse(
                sessionId,
                gameId,
                attemptsCount,
                maxAttemptsCount,
                startedAt,
                status
        );
    }
}
