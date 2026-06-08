package com.koreanwordle.game.dto;

import java.time.LocalDateTime;

public record GameResponse(
        Long gameId,
        Integer attemptsCount,
        Integer maxAttemptsCount,
        LocalDateTime startedAt,
        String status
) {
    public static GameResponse of(
            Long gameId,
            Integer attemptsCount,
            Integer maxAttemptsCount,
            LocalDateTime startedAt,
            String status
    ) {
        return new GameResponse(
                gameId,
                attemptsCount,
                maxAttemptsCount,
                startedAt,
                status
        );
    }
}
