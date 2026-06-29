const WORD_LENGTH = 3;
const board = document.getElementById("board");
const toast = document.getElementById("toast");
const guessForm = document.getElementById("guessForm");
const guessInput = document.getElementById("guessInput");
const dailyGameButton = document.getElementById("dailyGameButton");
const randomGameButton = document.getElementById("randomGameButton");
const resultModal = document.getElementById("resultModal");
const resultCloseButton = document.getElementById("resultCloseButton");
const resultMessage = document.getElementById("resultMessage");
const resultAnswer = document.getElementById("resultAnswer");
const resultDefinition = document.getElementById("resultDefinition");
const jamoTracker = document.getElementById("jamoTracker");

let currentGame = null;
let currentRow = 0;
let maxAttempts = 6;
let isComposing = false;

const statePriority = {
    ABSENT: 1,
    PRESENT: 2,
    CORRECT: 3
};

const CHOSEONG = ["ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"];
const JUNGSEONG = ["ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ", "ㅔ", "ㅕ", "ㅖ", "ㅗ", "ㅘ", "ㅙ", "ㅚ", "ㅛ", "ㅜ", "ㅝ", "ㅞ", "ㅟ", "ㅠ", "ㅡ", "ㅢ", "ㅣ"];
const JONGSEONG = ["ㄱ", "ㄲ", "ㄳ", "ㄴ", "ㄵ", "ㄶ", "ㄷ", "ㄹ", "ㄺ", "ㄻ", "ㄼ", "ㄽ", "ㄾ", "ㄿ", "ㅀ", "ㅁ", "ㅂ", "ㅄ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"];
const JAMO_GROUPS = {
    ONSET: CHOSEONG,
    NUCLEUS: JUNGSEONG,
    CODA: JONGSEONG
};

function initializeBoard(rows = maxAttempts) {
    board.innerHTML = "";
    board.style.gridTemplateRows = `repeat(${rows}, 62px)`;

    for (let row = 0; row < rows; row += 1) {
        for (let col = 0; col < WORD_LENGTH; col += 1) {
            const tile = document.createElement("div");
            tile.className = "tile";
            tile.dataset.row = String(row);
            tile.dataset.col = String(col);
            board.appendChild(tile);
        }
    }
}

function initializeJamoTracker() {
    Object.entries(JAMO_GROUPS).forEach(([type, jamos]) => {
        const list = jamoTracker.querySelector(`[data-jamo-type="${type}"]`);
        if (!list) {
            return;
        }

        list.innerHTML = "";
        jamos.forEach((jamo) => {
            const item = document.createElement("span");
            item.className = "jamo-tracker-item";
            item.dataset.jamoValue = jamo;
            item.dataset.hint = "";
            item.textContent = jamo;
            item.title = `${jamo}: 미사용`;
            item.addEventListener("animationend", () => {
                if (item.dataset.hint === "ABSENT") {
                    item.classList.add("removed");
                }
                item.classList.remove("dismissing");
            });
            list.appendChild(item);
        });
    });
}

function resetJamoTrackerState() {
    jamoTracker.querySelectorAll("[data-jamo-value]").forEach((item) => {
        item.dataset.hint = "";
        item.classList.remove("correct", "present", "absent", "dismissing", "removed");
        item.title = `${item.dataset.jamoValue}: 미사용`;
    });
}

function showToast(message, isError = false) {
    toast.textContent = message;
    toast.classList.toggle("error", isError);
    toast.classList.add("visible");

    window.clearTimeout(showToast.timer);
    showToast.timer = window.setTimeout(() => {
        toast.classList.remove("visible");
    }, 2200);
}

function openResultModal(response, solved) {
    resultMessage.textContent = solved ? "정답입니다." : "시도 횟수를 모두 사용했습니다.";
    resultAnswer.textContent = response.correctAnswer || "-";
    resultDefinition.textContent = response.definition || "뜻 정보가 없습니다.";
    resultModal.hidden = false;
}

function closeResultModal() {
    resultModal.hidden = true;
}

async function requestJson(url, options = {}) {
    const response = await fetch(url, {
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        },
        ...options
    });

    if (!response.ok) {
        let message = "요청을 처리할 수 없습니다.";
        try {
            const error = await response.json();
            message = error.message || message;
        } catch (_ignored) {
            message = response.statusText || message;
        }
        throw new Error(message);
    }

    return response.json();
}

function applyGameResponse(game) {
    currentGame = game;
    currentRow = 0;
    maxAttempts = game.maxAttemptsCount || 6;
    initializeBoard(maxAttempts);
    resetJamoTrackerState();
    guessInput.value = "";
    guessInput.disabled = false;
    guessInput.focus();
}

async function startDailyGame() {
    const game = await requestJson("/api/wordleKR/dailyGame");
    dailyGameButton.classList.add("active");
    randomGameButton.classList.remove("active");
    applyGameResponse(game);
    showToast("오늘의 문제를 시작합니다.");
}

async function startRandomGame() {
    const game = await requestJson("/api/wordleKR/createGame", {
        method: "POST"
    });
    randomGameButton.classList.add("active");
    dailyGameButton.classList.remove("active");
    applyGameResponse(game);
    showToast("랜덤 문제를 시작합니다.");
}

function tileFor(row, col) {
    return board.querySelector(`[data-row="${row}"][data-col="${col}"]`);
}

function normalizeKoreanInput(value) {
    return value.replace(/[^가-힣]/g, "").slice(0, WORD_LENGTH);
}

function syncGuessInput(force = false) {
    if (!force && isComposing) {
        renderInputPreview(normalizeKoreanInput(guessInput.value));
        return;
    }

    const normalized = normalizeKoreanInput(guessInput.value.trim());
    if (guessInput.value !== normalized) {
        guessInput.value = normalized;
    }
    renderInputPreview(normalized);
}

function renderTileLetter(tile, letter) {
    tile.innerHTML = "";

    if (!letter) {
        return;
    }

    const letterElement = document.createElement("span");
    letterElement.className = "tile-letter";
    letterElement.textContent = letter;
    tile.appendChild(letterElement);
}

function renderInputPreview(word) {
    for (let col = 0; col < WORD_LENGTH; col += 1) {
        const tile = tileFor(currentRow, col);
        if (!tile) {
            continue;
        }

        renderTileLetter(tile, word[col] || "");
    }
}

function renderCurrentInput() {
    renderInputPreview(normalizeKoreanInput(guessInput.value));
}

function normalizeHintName(hint) {
    return String(hint || "ABSENT").toLowerCase();
}

function strongestHint(jamos) {
    return jamos.reduce((strongest, jamo) => {
        const current = jamo.hint || "ABSENT";
        return statePriority[current] > statePriority[strongest] ? current : strongest;
    }, "ABSENT");
}

function renderGuess(response, submittedWord) {
    const row = currentRow;

    response.results.forEach((result, col) => {
        const tile = tileFor(row, col);
        if (!tile) {
            return;
        }

        const jamos = result.jamos || [];
        const tileState = normalizeHintName(strongestHint(jamos));

        tile.className = `tile filled ${tileState}`;
        tile.style.setProperty("--tile-delay", `${col * 130}ms`);
        renderTileLetter(tile, result.syllable || submittedWord[col] || "");

        const strip = document.createElement("div");
        strip.className = "jamo-strip";
        jamos.forEach((jamo, jamoIndex) => {
            const marker = document.createElement("span");
            marker.className = `jamo-state ${normalizeHintName(jamo.hint)}`;
            marker.style.setProperty("--jamo-delay", `${col * 130 + 260 + jamoIndex * 90}ms`);
            marker.title = `${jamo.type}: ${jamo.value} / ${jamo.hint}`;
            strip.appendChild(marker);
            updateJamoTracker(jamo.type, jamo.value, jamo.hint);
        });
        tile.appendChild(strip);
    });

    currentRow += 1;
}

function updateJamoTracker(type, value, hint) {
    if (!type || !value) {
        return;
    }

    const list = jamoTracker.querySelector(`[data-jamo-type="${type}"]`);
    const item = Array.from(list?.querySelectorAll("[data-jamo-value]") || [])
            .find((candidate) => candidate.dataset.jamoValue === value);
    if (!item) {
        return;
    }

    const nextPriority = statePriority[hint] || 0;
    const currentHint = item.dataset.hint || "";
    const currentPriority = statePriority[currentHint] || 0;

    if (nextPriority >= currentPriority) {
        item.dataset.hint = hint;
        if (hint !== "ABSENT") {
            item.classList.remove("dismissing", "removed");
        }
        item.classList.remove("correct", "present", "absent");
        item.classList.add(normalizeHintName(hint));
        item.title = `${value}: ${hint}`;

        if (hint === "ABSENT" && !item.classList.contains("removed")) {
            item.classList.add("dismissing");
        }
    }
}

async function submitGuess(event) {
    event.preventDefault();

    if (!currentGame) {
        showToast("게임을 먼저 시작해 주세요.", true);
        return;
    }

    syncGuessInput(true);
    const submittedWord = guessInput.value;

    if (submittedWord.length !== WORD_LENGTH) {
        showToast(`${WORD_LENGTH}글자 단어를 입력해 주세요.`, true);
        return;
    }

    try {
        const response = await requestJson("/api/wordleKR/submitAnswer", {
            method: "POST",
            body: JSON.stringify({
                gameId: currentGame.gameId,
                submittedWord,
                attemptNumber: currentRow + 1
            })
        });

        renderGuess(response, submittedWord);
        guessInput.value = "";

        if (response.correct) {
            showToast("정답입니다.");
            guessInput.disabled = true;
            openResultModal(response, true);
        } else if (currentRow >= maxAttempts) {
            showToast("시도 횟수를 모두 사용했습니다.", true);
            guessInput.disabled = true;
            openResultModal(response, false);
        } else {
            showToast("힌트를 확인해 보세요.");
        }
    } catch (error) {
        showToast(error.message, true);
    }
}

guessForm.addEventListener("submit", submitGuess);
guessInput.addEventListener("compositionstart", () => {
    isComposing = true;
});
guessInput.addEventListener("compositionend", () => {
    isComposing = false;
    syncGuessInput(true);
});
guessInput.addEventListener("input", (event) => {
    if (event.isComposing || isComposing) {
        renderCurrentInput();
        return;
    }

    syncGuessInput();
});
dailyGameButton.addEventListener("click", () => startDailyGame().catch((error) => showToast(error.message, true)));
randomGameButton.addEventListener("click", () => startRandomGame().catch((error) => showToast(error.message, true)));
resultCloseButton.addEventListener("click", closeResultModal);
resultModal.addEventListener("click", (event) => {
    if (event.target === resultModal) {
        closeResultModal();
    }
});

initializeJamoTracker();
initializeBoard();
startDailyGame().catch((error) => showToast(error.message, true));

