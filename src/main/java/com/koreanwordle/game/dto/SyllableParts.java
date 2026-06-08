package com.koreanwordle.game.dto;

public record SyllableParts(
        String original,
        String onset,
        String nucleus,
        String coda
) { }
