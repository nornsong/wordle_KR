package com.koreanwordle.game.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "게임 시작 시 응답")
public record GameResponse(
        @Schema(description = "게임 ID", example = "12")
        Long gameId,
        @Schema(description = "제출 횟수", example = "1")
        Integer attemptsCount,
        @Schema(description = "최대 제출 가능 횟수", example = "7")
        Integer maxAttemptsCount,
        @Schema(description = "시작 시간", example = "2026-06-14 18:59:10.026770")
        LocalDateTime startedAt,
        @Schema(description = "게임상태", example = "IN_PROGRESS")
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
