package com.koreanwordle.game.dto;

import java.util.List;

public record GuessResponse(
        String submittedWord,
        boolean correct,
        List<SyllableHint> results
) {
    public record SyllableHint(
            int index,
            String submittedSyllable,
            SyllableStatus syllableStatus,
            List<JamoHint> jamoResults
    ) { }

    public record JamoHint(
            JamoType type,
            String submitted,
            HintStatus status
    ) {}
    
    public enum SyllableStatus {
        CORRECT, // 정답
        PRESENT, // 는 존재하지만 위치가 다름
        ABSENT   // 단어가 존재하지 않음
    }
    
    public enum JamoType {
        ONSET,
        NUCLEUS,
        CODA
    }
    
    public enum HintStatus {
        CORRECT, // 정답
        PRESENT, // 는 존재하지만 위치가 다름
        ABSENT   // 단어가 존재하지 않음
    }
}


