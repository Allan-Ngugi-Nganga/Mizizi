package com.example.mizizi.domain

import com.example.mizizi.data.model.ProverbEntity

/** What the practice screen needs to render one question and its score. */
data class SessionState(
    val questionNumber: Int = 0,
    val totalQuestions: Int = 0,
    val score: Int = 0,
    val current: ProverbEntity? = null,
    val finished: Boolean = false
) {
    val isActive: Boolean get() = totalQuestions > 0 && !finished
}

/** The outcome of the attempt that was just submitted. */
data class AttemptOutcome(
    val proverb: ProverbEntity,
    val answer: String,
    val grade: Grade,
    val matchedKeywords: List<String>
)

/**
 * The practice session as a plain Kotlin state machine: no Android, no
 * database, no coroutines. [com.example.mizizi.ui.viewmodel.MiziziViewModel]
 * owns one of these and turns its results into LiveData, which is what makes
 * the session rules unit testable.
 */
class SessionEngine(private val sessionSize: Int = DEFAULT_SESSION_SIZE) {

    private var queue: List<ProverbEntity> = emptyList()

    var state: SessionState = SessionState()
        private set

    /** Starts a fresh session of up to [sessionSize] random proverbs. */
    fun start(proverbs: List<ProverbEntity>): SessionState {
        if (proverbs.isEmpty()) {
            queue = emptyList()
            state = SessionState(finished = true)
            return state
        }
        queue = proverbs.shuffled().take(sessionSize)
        state = SessionState(
            questionNumber = 1,
            totalQuestions = queue.size,
            score = 0,
            current = queue.first(),
            finished = false
        )
        return state
    }

    /**
     * Grades [answer] against the current proverb and updates the score.
     * Returns null when there is no question in play.
     */
    fun submit(answer: String): AttemptOutcome? {
        val proverb = state.current ?: return null

        val grade = if (answer.isBlank()) {
            Grade.INCORRECT
        } else {
            Grader.grade(proverb.keywordList(), proverb.meaning, answer)
        }
        val matched = if (answer.isBlank()) {
            emptyList()
        } else {
            Grader.matchedKeywords(proverb.keywordList(), answer)
        }

        if (grade == Grade.CORRECT) {
            state = state.copy(score = state.score + 1)
        }
        return AttemptOutcome(proverb, answer, grade, matched)
    }

    /** Moves to the next proverb, or marks the session finished. */
    fun next(): SessionState {
        val nextIndex = state.questionNumber
        state = if (nextIndex >= queue.size) {
            state.copy(finished = true, current = null)
        } else {
            state.copy(questionNumber = nextIndex + 1, current = queue[nextIndex])
        }
        return state
    }

    /** Drops the session back to its initial, empty state. */
    fun reset(): SessionState {
        queue = emptyList()
        state = SessionState()
        return state
    }

    companion object {
        const val DEFAULT_SESSION_SIZE = 5
    }
}
