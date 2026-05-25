package com.koreanwordle.game.service;

import com.koreanwordle.game.domain.Game;
import com.koreanwordle.game.domain.GameSession;
import com.koreanwordle.game.domain.Word;
import com.koreanwordle.game.dto.GameSessionResponse;
import com.koreanwordle.game.dto.GuessResponse;
import com.koreanwordle.game.repository.GameRepository;
import com.koreanwordle.game.repository.GameSessionRepository;
import com.koreanwordle.game.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.koreanwordle.game.domain.GameType.DAILY;

@Service
@RequiredArgsConstructor
public class GameSessionServiceImpl implements GameSessionService {

    private final WordRepository wordRepository;
    private final GameSessionRepository gameSessionRepository;
    private final GameRepository gameRepository;

    @Override
    @Transactional
    public GameSessionResponse getCreateDailyGame(String userId) {

        LocalDate today = LocalDate.now();

        Game game = gameRepository.getDailyGame(DAILY, today)
                .orElseGet(() -> {
                    Word word = wordRepository.findRandomWord()
                            .orElseThrow(() -> new IllegalStateException("등록된 단어가 없습니다."));

                    return gameRepository.save(Game.dailyGame(word, today));
                });

        GameSession session = gameSessionRepository.findDailyGame(userId, game)
                .orElseGet(() -> gameSessionRepository.save(new GameSession(userId, game)));

        return GameSessionResponse.of(
                session.getId(),
                session.getGame().getId(),
                session.getAttemptsCount(),
                session.getMaxAttemptsCount(),
                session.getStartedAt(),
                session.getStatus().name()
        );
    }

    @Override
    @Transactional
    public GameSessionResponse getCreateRandomGame(String userId) {

        Word word = wordRepository.findRandomWord()
                .orElseThrow(() -> new IllegalStateException("등록된 단어가 없습니다."));

        Game game = gameRepository.save(Game.randomGame(word));

        GameSession session = gameSessionRepository.save(new GameSession(userId, game));

        return GameSessionResponse.of(
                session.getId(),
                session.getGame().getId(),
                session.getAttemptsCount(),
                session.getMaxAttemptsCount(),
                session.getStartedAt(),
                session.getStatus().name()
        );
    }
    
    @Override
    @Transactional
    public GuessResponse submitAnswer(String userId, Long sessionId, String submittedWord) {

        String correctWord = gameSessionRepository.findSessionAndGame(userId, sessionId)
                .orElseThrow(() -> new IllegalArgumentException("게임 세션을 찾을 수 없습니다."));

        boolean correct = submittedWord.equals(correctWord);

        return new GuessResponse(
                submittedWord,
                correct,
                List.of()
        );
    }
}

