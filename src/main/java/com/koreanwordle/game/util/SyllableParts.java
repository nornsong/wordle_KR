package com.koreanwordle.game.util;

public record SyllableParts(
        String original,
        String onset,
        String nucleus,
        String coda
) { }
