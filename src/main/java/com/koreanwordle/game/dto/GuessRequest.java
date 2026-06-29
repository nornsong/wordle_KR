package com.koreanwordle.game.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "정답 제출 요청")
public record GuessRequest(
        @Schema(description = "게임 ID", example = "12")
        @NotNull(message = "gameId는 필수입니다.")
        Long gameId,

        @Schema(description = "정답 제출 단어", example = "소나무")
        @NotBlank(message = "submittedWord는 필수입니다.")
        String submittedWord,

        @Schema(description = "현재 제출 시도 번호", example = "1")
        @NotNull(message = "attemptNumber는 필수입니다.")
        Integer attemptNumber
) { }