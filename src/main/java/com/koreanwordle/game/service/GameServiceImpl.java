package com.koreanwordle.game.service;

import com.koreanwordle.game.domain.Game;
import com.koreanwordle.game.domain.Word;
import com.koreanwordle.game.dto.GameResponse;
import com.koreanwordle.game.dto.GuessResponse;
import com.koreanwordle.game.exception.CustomException;
import com.koreanwordle.game.exception.ErrorCode;
import com.koreanwordle.game.repository.GameRepository;
import com.koreanwordle.game.repository.WordRepository;
import com.koreanwordle.game.rules.HintType;
import com.koreanwordle.game.rules.SyllableType;
import com.koreanwordle.game.util.HangulUtils;
import com.koreanwordle.game.dto.SyllableParts;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.koreanwordle.game.domain.GameType.DAILY;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final WordRepository wordRepository;
    private final GameRepository gameRepository;

    @Override
    @Transactional
    public GameResponse getCreateDailyGame() {

        LocalDate today = LocalDate.now();

        Game game = gameRepository.getDailyGame(DAILY, today)
                .orElseGet(() -> {
                    Word word = wordRepository.findRandomWord(PageRequest.of(0, 1))
                            .stream()
                            .findFirst()
                            .orElseThrow(() -> new CustomException(ErrorCode.WORD_NOT_FOUND));

                    return gameRepository.save(Game.createDailyGame(word, today));
                });

        return GameResponse.of(
                game.getId(),
                game.getAttemptsCount(),
                game.getMaxAttemptsCount(),
                game.getStartedAt(),
                game.getStatus().name()
        );
    }

    @Override
    @Transactional
    public GameResponse getCreateRandomGame() {

        Word word = wordRepository.findRandomWord(PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.WORD_NOT_FOUND));

        Game game = gameRepository.save(Game.createRandomGame(word));

        return GameResponse.of(
                game.getId(),
                game.getAttemptsCount(),
                game.getMaxAttemptsCount(),
                game.getStartedAt(),
                game.getStatus().name()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public GuessResponse submitAnswer(Long gameId, String submittedWord) {

        String correctWord = gameRepository.findNormalizedAnswerWord(gameId)
                .orElseThrow(() -> new CustomException(ErrorCode.GAME_NOT_FOUND));

        if (submittedWord.length() != correctWord.length()) {
            throw new CustomException(ErrorCode.INVALID_WORD_LENGTH);
        }

        if (!submittedWord.matches("^[가-힣]+$")) {
            throw new CustomException(ErrorCode.INVALID_WORD_FORMAT);
        }

        if (!wordRepository.findWordInList(submittedWord)) {
            throw new CustomException(ErrorCode.WORD_NOT_IN_DICTIONARY);
        }

        // util 집어넣기
        List<SyllableParts> userWord = HangulUtils.decomposeWord(submittedWord);
        List<SyllableParts> questionWord = HangulUtils.decomposeWord(correctWord);

        boolean correct = submittedWord.equals(correctWord);


        List<GuessResponse.SyllableHint> results = buildHints(
                        userWord,       // 사용자 단어 철자 분해
                        questionWord    // 문제의 단어 철자 분해
        );

        return GuessResponse.of(
                correct,
                results
        );
    }

    private List<GuessResponse.SyllableHint> buildHints(
            List<SyllableParts> userWord,
            List<SyllableParts> questionWord
    ) {
        List<GuessResponse.SyllableHint> results = new ArrayList<>();

        boolean[] usedAnswerOnset = new boolean[userWord.size()];
        boolean[] usedAnswerNucleus = new boolean[userWord.size()];
        boolean[] usedAnswerCoda = new boolean[userWord.size()];

        for (int i = 0; i < userWord.size(); i++) {
            SyllableParts user = userWord.get(i);

            HintType onsetStatus = HintType.ABSENT;
            HintType nucleusStatus = HintType.ABSENT;
            HintType codaStatus = HintType.ABSENT;

            for (int j = 0; j < questionWord.size(); j++) {
                SyllableParts answer = questionWord.get(j);

                if (user.onset().equals(answer.onset()) && !usedAnswerOnset[j]) {
                    if (i == j) {
                        onsetStatus = HintType.CORRECT;
                        usedAnswerOnset[j] = true;
                    }
                    else if(onsetStatus != HintType.CORRECT) {
                        onsetStatus = HintType.PRESENT;
                    }
                }
                if (user.nucleus().equals(answer.nucleus()) && !usedAnswerNucleus[j]) {
                    if (i == j) {
                        nucleusStatus = HintType.CORRECT;
                        usedAnswerNucleus[j] = true;
                    }
                    else if(nucleusStatus != HintType.CORRECT) {
                        nucleusStatus = HintType.PRESENT;
                    }
                }
                if (user.coda().equals(answer.coda()) && !usedAnswerCoda[j]) {
                    if (i == j) {
                        codaStatus = HintType.CORRECT;
                        usedAnswerCoda[j] = true;
                    }
                    else if(codaStatus != HintType.CORRECT) {
                        codaStatus = HintType.PRESENT;
                    }
                }
            }
            List<GuessResponse.JamoHint> jamos = List.of(
                    GuessResponse.JamoHint.of(SyllableType.ONSET, user.onset(), onsetStatus),
                    GuessResponse.JamoHint.of(SyllableType.NUCLEUS, user.nucleus(), nucleusStatus),
                    GuessResponse.JamoHint.of(SyllableType.CODA, user.coda(), codaStatus)
            );

            results.add(GuessResponse.SyllableHint.of(
                    i,
                    user.original(),
                    jamos
            ));

        }
        return List.copyOf(results);
    }
}