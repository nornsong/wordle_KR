package com.koreanwordle.game.util;

import com.koreanwordle.game.exception.CustomException;
import com.koreanwordle.game.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;

public class HangulUtils {

    private HangulUtils() {}

    // 한글 음절을 초성/중성/종성으로 분해
    public static SyllableParts decompose(char syllable) {
        if(syllable < '가' || syllable > '힣') {
            throw new CustomException(ErrorCode.INVALID_WORD_FORMAT);
        }

        int base = syllable - '가';

        int onsetIndex = base / (21 * 28);
        int nucleusIndex = (base % (21 * 28)) / 28;
        int codaIndex = base % 28;

        return new SyllableParts(
                String.valueOf(syllable),
                ONSETS[onsetIndex],
                NUCLEI[nucleusIndex],
                CODAS[codaIndex]
        );
    }

    public static List<SyllableParts> decomposeWord(String word) {
        List<SyllableParts> parts = new ArrayList<>();

        for(int i = 0; i < word.length(); i++) {
            parts.add(decompose(word.charAt(i)));
        }

        return parts;
    }

    private static final String[] ONSETS = {
            "ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ",
            "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"
    };

    private static final String[] NUCLEI = {
            "ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ", "ㅔ", "ㅕ", "ㅖ", "ㅗ",
            "ㅘ", "ㅙ", "ㅚ", "ㅛ", "ㅜ", "ㅝ", "ㅞ", "ㅟ", "ㅠ",
            "ㅡ", "ㅢ", "ㅣ"
    };

    private static final String[] CODAS = {
            "", "ㄱ", "ㄲ", "ㄳ", "ㄴ", "ㄵ", "ㄶ", "ㄷ", "ㄹ",
            "ㄺ", "ㄻ", "ㄼ", "ㄽ", "ㄾ", "ㄿ", "ㅀ", "ㅁ", "ㅂ",
            "ㅄ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"
    };
}
