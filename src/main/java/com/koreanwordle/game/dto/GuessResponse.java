package com.koreanwordle.game.dto;

import com.koreanwordle.game.rules.HintType;
import com.koreanwordle.game.rules.SyllableType;

import java.util.List;

public record GuessResponse(
        boolean correct,
        String correctAnswer,
        String definition,
        List<SyllableHint> results
) {
    public static GuessResponse of(
            boolean correct,
            String correctAnswer,
            String definition,
            List<SyllableHint> results
    ) {
        return new GuessResponse(correct, correctAnswer, definition, results);
    }

    public record SyllableHint(
            int index,
            String syllable,
            List<JamoHint> jamos
    ) {
        public static SyllableHint of(
                int index,
                String syllable,
                List<JamoHint> jamos
        ) {
            return new SyllableHint(index, syllable, jamos);
        }
    }

    public record JamoHint(
            SyllableType type,
            String value,
            HintType hint
    ) {
        public static JamoHint of(
                SyllableType type,
                String value,
                HintType hint
        ) {
            return new JamoHint(type, value, hint);
        }
    }
}


