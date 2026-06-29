package com.koreanwordle.game.dto;

import com.koreanwordle.game.rules.HintType;
import com.koreanwordle.game.rules.SyllableType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "정답 제출 결과 응답")
public record GuessResponse(
        @Schema(description = "제출한 단어가 정답인지 여부", example = "false")
        boolean correct,

        @Schema(description = "게임이 종료된 경우 반환되는 정답 단어", example = "항공기", nullable = true)
        String correctAnswer,

        @Schema(
                description = "게임이 종료된 경우 반환되는 정답 단어의 뜻",
                example = "사람이나 물건을 싣고 공중을 비행할 수 있는 탈것을 통틀어 이르는 말.",
                nullable = true
        )
        String definition,

        @Schema(description = "제출 단어의 각 글자에 대한 초성, 중성, 종성 힌트")
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

    @Schema(description = "한 글자 단위의 힌트 결과")
    public record SyllableHint(
            @Schema(description = "제출 단어에서의 글자 위치. 0부터 시작", example = "0")
            int index,

            @Schema(description = "제출한 글자", example = "소")
            String syllable,

            @Schema(description = "해당 글자의 초성, 중성, 종성 힌트")
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

    @Schema(description = "초성, 중성, 종성 단위의 힌트 결과")
    public record JamoHint(
            @Schema(description = "자모 위치 유형", example = "ONSET", allowableValues = {"ONSET", "NUCLEUS", "CODA"})
            SyllableType type,

            @Schema(description = "비교 대상 자모 값. 받침이 없으면 빈 문자열", example = "ㅅ")
            String value,

            @Schema(description = "힌트 결과", example = "ABSENT", allowableValues = {"CORRECT", "PRESENT", "ABSENT"})
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


