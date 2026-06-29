<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="naver-site-verification" content="baf78029da25beb85568a2fd15bd90177692e838">
    <title>하루낱말</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/css/k-wordle.css">
</head>
<body>
<main class="app-shell">
    <aside class="help-panel" aria-label="게임 방법">
        <h2>게임 방법</h2>
        <div class="help-content">
            <p>세 글자 낱말을 입력해 정답을 맞혀 보세요.</p>
            <p>예를 들어 정답이 <strong>항공기</strong>이고 입력이 <strong>소나무</strong>라면, 각 글자의 초성, 중성, 종성을 따로 비교합니다.</p>
            <p>초성은 첫 자음, 중성은 모음, 종성은 받침입니다. 각 글자 아래의 세 칸은 위에서부터 초성, 중성, 종성 힌트를 뜻합니다.</p>
            <p>초성은 정답의 초성과만, 중성은 정답의 중성과만, 종성은 정답의 종성과만 비교합니다.</p>
            <p>사용자가 제출한 받침이 없는 글자에 종성 힌트가 초록색으로 나오는 것은 <strong>해당 문제의 정답이 받침이 없는 단어</strong>입니다.</p>
            <p>문제의 정답들은 국립국어원 표준국어대사전의 명사 단어들만 제공됩니다.</p>
            <div class="example-board" aria-label="힌트 예시">
                <div class="example-tile">
                    <span>소</span>
                    <div class="jamo-strip">
                        <span class="jamo-state absent"></span>
                        <span class="jamo-state present"></span>
                        <span class="jamo-state present"></span>
                    </div>
                </div>
                <div class="example-tile">
                    <span>나</span>
                    <div class="jamo-strip">
                        <span class="jamo-state absent"></span>
                        <span class="jamo-state present"></span>
                        <span class="jamo-state present"></span>
                    </div>
                </div>
                <div class="example-tile">
                    <span>무</span>
                    <div class="jamo-strip">
                        <span class="jamo-state absent"></span>
                        <span class="jamo-state absent"></span>
                        <span class="jamo-state correct"></span>
                    </div>
                </div>
            </div>
            <div class="help-list">
                <span><i class="dot correct"></i>초록: 같은 위치에 같은 자모가 있음</span>
                <span><i class="dot present"></i>노랑: 정답에 있지만 위치가 다름</span>
                <span><i class="dot absent"></i>진회색: 정답에 없는 자모</span>
            </div>
        </div>
    </aside>

    <div class="game-shell">
        <header class="game-header">
            <div class="brand">
                <span>하루낱말</span>
            </div>
            <div class="header-actions">
                <a class="top-nav-link" href="/intro">소개</a>
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
                        maxlength="3"
                        placeholder="여기에 정답을 제출"
                        aria-label="제출 단어">
                <button type="submit" class="submit-button">ENTER</button>
            </form>
        </section>

    </div>

    <aside id="jamoTracker" class="jamo-tracker-panel" aria-label="초성 중성 종성 사용 현황">
        <h2>남은 자모 현황</h2>
        <div class="jamo-tracker">
            <div class="jamo-tracker-row">
                <strong class="jamo-tracker-label">초성</strong>
                <div class="jamo-tracker-list" data-jamo-type="ONSET"></div>
            </div>
            <div class="jamo-tracker-row">
                <strong class="jamo-tracker-label">중성</strong>
                <div class="jamo-tracker-list" data-jamo-type="NUCLEUS"></div>
            </div>
            <div class="jamo-tracker-row">
                <strong class="jamo-tracker-label">종성</strong>
                <div class="jamo-tracker-list" data-jamo-type="CODA"></div>
            </div>
        </div>
    </aside>
</main>

<div id="resultModal" class="help-modal" role="dialog" aria-modal="true" aria-labelledby="resultTitle" hidden>
    <div class="help-dialog result-dialog">
        <div class="help-header">
            <h2 id="resultTitle">결과</h2>
            <button id="resultCloseButton" type="button" class="icon-button" title="닫기" aria-label="닫기">×</button>
        </div>
        <div class="result-content">
            <p id="resultMessage" class="result-message"></p>
            <dl>
                <div>
                    <dt>정답</dt>
                    <dd id="resultAnswer"></dd>
                </div>
                <div>
                    <dt>뜻</dt>
                    <dd id="resultDefinition"></dd>
                </div>
            </dl>
        </div>
    </div>
</div>

<script src="/js/k-wordle.js"></script>
</body>
</html>

