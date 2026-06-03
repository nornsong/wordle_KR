const WORD_LENGTH = 4;
const board = document.getElementById("board");
const toast = document.getElementById("toast");
const guessForm = document.getElementById("guessForm");
const guessInput = document.getElementById("guessInput");
const dailyGameButton = document.getElementById("dailyGameButton");
const randomGameButton = document.getElementById("randomGameButton");

let currentGame = null;
let currentRow = 0;
let maxAttempts = 6;

const statePriority = {
    ABSENT: 1,
    PRESENT: 2,
    CORRECT: 3
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

function resetKeyboardState() {
    document.querySelectorAll(".keyboard [data-key]").forEach((key) => {
        key.dataset.hint = "";
        key.classList.remove("correct", "present", "absent");
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
    resetKeyboardState();
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

function renderInputPreview(word) {
    for (let col = 0; col < WORD_LENGTH; col += 1) {
        const tile = tileFor(currentRow, col);
        if (!tile) {
            continue;
        }

        tile.textContent = word[col] || "";
        tile.classList.toggle("filled", Boolean(word[col]));
    }
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

        tile.className = `tile ${tileState}`;
        tile.textContent = result.syllable || submittedWord[col] || "";

        const strip = document.createElement("div");
        strip.className = "jamo-strip";
        jamos.forEach((jamo) => {
            const marker = document.createElement("span");
            marker.className = `jamo-state ${normalizeHintName(jamo.hint)}`;
            marker.title = `${jamo.type}: ${jamo.value} / ${jamo.hint}`;
            strip.appendChild(marker);
            updateKeyboardKey(jamo.value, jamo.hint);
        });
        tile.appendChild(strip);
    });

    currentRow += 1;
}

function updateKeyboardKey(value, hint) {
    const key = document.querySelector(`[data-key="${CSS.escape(value)}"]`);
    if (!key) {
        return;
    }

    const nextPriority = statePriority[hint] || 0;
    const currentHint = key.dataset.hint || "";
    const currentPriority = statePriority[currentHint] || 0;

    if (nextPriority >= currentPriority) {
        key.dataset.hint = hint;
        key.classList.remove("correct", "present", "absent");
        key.classList.add(normalizeHintName(hint));
    }
}

async function submitGuess(event) {
    event.preventDefault();

    if (!currentGame) {
        showToast("게임을 먼저 시작해 주세요.", true);
        return;
    }

    const submittedWord = guessInput.value.trim();
    if (submittedWord.length !== WORD_LENGTH) {
        showToast(`${WORD_LENGTH}글자 단어를 입력해 주세요.`, true);
        return;
    }

    try {
        const response = await requestJson("/api/wordleKR/submitAnswer", {
            method: "POST",
            body: JSON.stringify({
                gameId: currentGame.gameId,
                submittedWord
            })
        });

        renderGuess(response, submittedWord);
        guessInput.value = "";

        if (response.correct) {
            showToast("정답입니다.");
            guessInput.disabled = true;
        } else if (currentRow >= maxAttempts) {
            showToast("시도 횟수를 모두 사용했습니다.", true);
            guessInput.disabled = true;
        } else {
            showToast("힌트를 확인해 보세요.");
        }
    } catch (error) {
        showToast(error.message, true);
    }
}

function handleKeyboardClick(event) {
    const button = event.target.closest("button");
    if (!button || guessInput.disabled) {
        return;
    }

    if (button.dataset.enter === "true") {
        guessForm.requestSubmit();
        return;
    }

    if (button.dataset.backspace === "true") {
        guessInput.value = guessInput.value.slice(0, -1);
        renderInputPreview(guessInput.value);
        return;
    }

    if (button.dataset.key && guessInput.value.length < WORD_LENGTH) {
        guessInput.value += button.dataset.key;
        renderInputPreview(guessInput.value);
    }
}

guessForm.addEventListener("submit", submitGuess);
guessInput.addEventListener("input", () => renderInputPreview(guessInput.value.trim()));
dailyGameButton.addEventListener("click", () => startDailyGame().catch((error) => showToast(error.message, true)));
randomGameButton.addEventListener("click", () => startRandomGame().catch((error) => showToast(error.message, true)));
document.querySelector(".keyboard").addEventListener("click", handleKeyboardClick);

initializeBoard();
startDailyGame().catch((error) => showToast(error.message, true));
