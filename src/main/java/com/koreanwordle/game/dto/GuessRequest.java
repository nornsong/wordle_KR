package com.koreanwordle.game.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GuessRequest(
        @NotNull(message = "gameId는 필수입니다.")
        Long gameId,

        @NotBlank(message = "submittedWord는 필수입니다.")
        String submittedWord
) { }