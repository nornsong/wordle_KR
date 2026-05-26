package com.koreanwordle.game.service;

import com.koreanwordle.game.domain.Game;
import com.koreanwordle.game.domain.Word;
import com.koreanwordle.game.dto.GameResponse;
import com.koreanwordle.game.dto.GuessResponse;
import com.koreanwordle.game.repository.GameRepository;
import com.koreanwordle.game.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
                    Word word = wordRepository.findRandomWord()
                            .orElseThrow(() -> new IllegalStateException("등록된 단어가 없습니다."));

                    return gameRepository.save(Game.dailyGame(word, today));
                });

        return GameResponse.of(
                game.getId(),
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

        Word word = wordRepository.findRandomWord()
                .orElseThrow(() -> new IllegalStateException("등록된 단어가 없습니다."));

        Game game = gameRepository.save(Game.randomGame(word));

        return GameResponse.of(
                game.getId(),
                game.getId(),
                game.getAttemptsCount(),
                game.getMaxAttemptsCount(),
                game.getStartedAt(),
                game.getStatus().name()
        );
    }
    
    @Override
    @Transactional
    public GuessResponse submitAnswer(Long gameId, String submittedWord) {

        String correctWord = gameRepository.findNormalizedAnswerWord(gameId)
                .orElseThrow(() -> new IllegalArgumentException("게임을 찾을 수 없습니다."));

        if(submittedWord.length() != correctWord.length()) {
            throw new IllegalArgumentException("제출 단어의 길이가 정답 단어와 다릅니다.");
        }

        if(!submittedWord.matches("^[가-힣]+$")) {
            throw new IllegalArgumentException("제출 단어는 완성형 한글만 입력할 수 있습니다.");
        }

        if(!wordRepository.findWordInList(submittedWord)) {
            throw new IllegalArgumentException("사전에 없는 단어입니다.");
        }









        boolean correct = submittedWord.equals(correctWord);

        return new GuessResponse(
                submittedWord,
                correct,
                List.of()
        );
    }
}