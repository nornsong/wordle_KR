<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>K-Wordle</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/css/k-wordle.css">
</head>
<body>
<main class="app-shell">
    <header class="game-header">
        <div class="brand">
            <span>K-</span>
            <span>Wordle</span>
        </div>
        <div class="status-legend" aria-label="힌트 색상 설명">
            <span><i class="dot correct"></i>#6aaa64</span>
            <span><i class="dot present"></i>#c9b458</span>
            <span><i class="dot absent"></i>#787c7e</span>
        </div>
        <div class="header-actions">
            <button type="button" class="icon-button" title="도움말" aria-label="도움말">?</button>
            <button type="button" class="icon-button" title="통계" aria-label="통계">▥</button>
        </div>
    </header>

    <section class="game-panel" aria-label="K-Wordle 게임 영역">
        <div id="toast" class="toast" role="status" aria-live="polite"></div>

        <div class="mode-bar" aria-label="게임 선택">
            <button id="dailyGameButton" type="button" class="mode-button active">오늘의 문제</button>
            <button id="randomGameButton" type="button" class="mode-button">랜덤 문제</button>
        </div>

        <div id="board" class="board" aria-label="정답 입력 결과 보드"></div>

        <form id="guessForm" class="guess-form" autocomplete="off">
            <input
                    id="guessInput"
                    class="guess-input"
                    name="submittedWord"
                    type="text"
                    inputmode="text"
                    maxlength="4"
                    placeholder="네 글자 단어"
                    aria-label="제출 단어">
            <button type="submit" class="submit-button">ENTER</button>
        </form>
    </section>

    <section class="keyboard" aria-label="한글 자모 키보드">
        <div class="key-row">
            <button type="button" data-key="ㅂ">ㅂ</button>
            <button type="button" data-key="ㅈ">ㅈ</button>
            <button type="button" data-key="ㄷ">ㄷ</button>
            <button type="button" data-key="ㄱ">ㄱ</button>
            <button type="button" data-key="ㅅ">ㅅ</button>
            <button type="button" data-key="ㅛ">ㅛ</button>
            <button type="button" data-key="ㅕ">ㅕ</button>
            <button type="button" data-key="ㅑ">ㅑ</button>
            <button type="button" data-key="ㅐ">ㅐ</button>
            <button type="button" data-key="ㅔ">ㅔ</button>
        </div>
        <div class="key-row">
            <button type="button" data-key="ㅁ">ㅁ</button>
            <button type="button" data-key="ㄴ">ㄴ</button>
            <button type="button" data-key="ㅇ">ㅇ</button>
            <button type="button" data-key="ㄹ">ㄹ</button>
            <button type="button" data-key="ㅎ">ㅎ</button>
            <button type="button" data-key="ㅗ">ㅗ</button>
            <button type="button" data-key="ㅓ">ㅓ</button>
            <button type="button" data-key="ㅏ">ㅏ</button>
            <button type="button" data-key="ㅣ">ㅣ</button>
        </div>
        <div class="key-row">
            <button type="button" class="wide" data-enter="true">ENTER</button>
            <button type="button" data-key="ㅋ">ㅋ</button>
            <button type="button" data-key="ㅌ">ㅌ</button>
            <button type="button" data-key="ㅊ">ㅊ</button>
            <button type="button" data-key="ㅍ">ㅍ</button>
            <button type="button" data-key="ㅠ">ㅠ</button>
            <button type="button" data-key="ㅜ">ㅜ</button>
            <button type="button" data-key="ㅡ">ㅡ</button>
            <button type="button" data-backspace="true" aria-label="지우기">⌫</button>
        </div>
    </section>
</main>
<script src="/js/k-wordle.js"></script>
</body>
</html>
