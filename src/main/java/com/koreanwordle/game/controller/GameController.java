package com.koreanwordle.game.controller;

import com.koreanwordle.game.dto.GameResponse;
import com.koreanwordle.game.dto.GuessRequest;
import com.koreanwordle.game.dto.GuessResponse;
import com.koreanwordle.game.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wordleKR")
@Tag(name = "게임 API", description = "하루낱말 게임 생성 및 정답 제출 API")
public class GameController {

    private final GameService gameService;

    // 오늘의 게임 생성
    @Operation(
            summary = "오늘의 게임 조회 또는 생성",
            description = "오늘의 날짜에 해당하는 DAILY 게임을 조회하고, 없으면 새로 생성합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "오늘의 게임 반환"),
            @ApiResponse(responseCode = "404", description = "등록된 단어가 없음")
    })
    @GetMapping("/dailyGame")
    public ResponseEntity<GameResponse> getDailyGame() {
        GameResponse response = gameService.getCreateDailyGame();
        return ResponseEntity.ok(response);
    }

    // 게임 생성
    @Operation(
            summary = "랜덤 게임 생성",
            description = "임의의 단어를 선택해 RANDOM 게임을 새로 생성합니다."
    )
    @ApiResponse(responseCode = "200", description = "랜덤 게임 생성 성공")
    @PostMapping("/createGame")
    public ResponseEntity<GameResponse> getCreateRandomGame() {
        GameResponse response = gameService.getCreateRandomGame();
        return ResponseEntity.ok(response);
    }
    
    // 사용자 정답 제출
    @Operation(
            summary = "정답 제출",
            description = "사용자가 입력한 단어를 제출하고 초성, 중성, 종성 단위의 힌트를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "제출 결과 반환"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 시도 횟수 초과"),
            @ApiResponse(responseCode = "404", description = "게임 또는 단어를 찾을 수 없음")
    })
    @PostMapping("/submitAnswer")
    public ResponseEntity<GuessResponse> submitAnswer(@Valid @RequestBody GuessRequest request) {
        GuessResponse response = gameService.submitAnswer(request.gameId(), request.submittedWord());
        return ResponseEntity.ok(response);
    }
}
