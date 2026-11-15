package com.example.mizizi.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mizizi.data.local.MiziziDatabase
import com.example.mizizi.data.model.AttemptEntity
import com.example.mizizi.data.model.ProverbEntity
import com.example.mizizi.data.remote.ApiResult
import com.example.mizizi.data.repository.MiziziRepository
import com.example.mizizi.domain.Grade
import com.example.mizizi.domain.Grader
import kotlinx.coroutines.launch

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

/** State of the optional literal-translation hint. */
sealed class HintState {
    object Idle : HintState()
    object Loading : HintState()
    data class Loaded(val translation: String) : HintState()
    data class Failed(val message: String) : HintState()
}

/** One place for validation problems the screens report back to the user. */
sealed class FormResult {
    object Saved : FormResult()
    data class Invalid(val message: String) : FormResult()
}

/**
 * Shared ViewModel for the whole app.
 *
 * It owns the practice session, the archive and the proverb editor actions, so
 * the Fragments stay thin: they collect input and observe LiveData.
 */
class MiziziViewModel(application: Application) : AndroidViewModel(application) {

    private val database = MiziziDatabase.getDatabase(application)
    val repository = MiziziRepository(database.proverbDao(), database.attemptDao())

    val proverbs: LiveData<List<ProverbEntity>> = repository.proverbs
    val attempts: LiveData<List<AttemptEntity>> = repository.attempts

    private val _session = MutableLiveData(SessionState())
    val session: LiveData<SessionState> = _session

    private val _outcome = MutableLiveData<AttemptOutcome?>()
    val outcome: LiveData<AttemptOutcome?> = _outcome

    private val _hint = MutableLiveData<HintState>(HintState.Idle)
    val hint: LiveData<HintState> = _hint

    private val _formResult = MutableLiveData<FormResult?>()
    val formResult: LiveData<FormResult?> = _formResult

    private var queue: List<ProverbEntity> = emptyList()

    /** Starts a fresh session of up to [SESSION_SIZE] random proverbs. */
    fun startSession() {
        val available = proverbs.value.orEmpty()
        if (available.isEmpty()) {
            _session.value = SessionState(finished = true)
            return
        }
        queue = available.shuffled().take(SESSION_SIZE)
        _outcome.value = null
        _hint.value = HintState.Idle
        _session.value = SessionState(
            questionNumber = 1,
            totalQuestions = queue.size,
            score = 0,
            current = queue.first(),
            finished = false
        )
    }

    /** Grades [answer], saves the attempt and publishes the outcome. */
    fun submitAnswer(answer: String) {
        val state = _session.value ?: return
        val proverb = state.current ?: return
        if (answer.isBlank()) {
            _outcome.value = AttemptOutcome(proverb, answer, Grade.INCORRECT, emptyList())
            return
        }

        val grade = Grader.grade(proverb.keywordList(), proverb.meaning, answer)
        val matched = Grader.matchedKeywords(proverb.keywordList(), answer)
        val updatedScore = if (grade == Grade.CORRECT) state.score + 1 else state.score

        _session.value = state.copy(score = updatedScore)
        _outcome.value = AttemptOutcome(proverb, answer, grade, matched)

        viewModelScope.launch {
            repository.recordAttempt(proverb, answer, grade.name)
        }
    }

    /** Moves to the next proverb, or marks the session finished. */
    fun nextQuestion() {
        val state = _session.value ?: return
        val nextIndex = state.questionNumber
        _outcome.value = null
        _hint.value = HintState.Idle

        if (nextIndex >= queue.size) {
            _session.value = state.copy(finished = true, current = null)
        } else {
            _session.value = state.copy(
                questionNumber = nextIndex + 1,
                current = queue[nextIndex]
            )
        }
    }

    /** Asks the repository for a literal translation of the current proverb. */
    fun requestHint() {
        val proverb = _session.value?.current ?: return
        if (_hint.value is HintState.Loading) return

        _hint.value = HintState.Loading
        viewModelScope.launch {
            _hint.value = when (val result = repository.literalTranslation(proverb.original)) {
                is ApiResult.Success -> HintState.Loaded(result.value)
                is ApiResult.Error -> HintState.Failed(result.message)
            }
        }
    }

    /**
     * Translates any proverb on demand and reports the outcome through
     * [onResult], so a screen can show a loading state while it waits.
     */
    fun requestMachineTranslation(original: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            val text = when (val result = repository.literalTranslation(original)) {
                is ApiResult.Success -> result.value
                is ApiResult.Error -> result.message
            }
            onResult(text)
        }
    }

    fun resetSession() {
        queue = emptyList()
        _outcome.value = null
        _hint.value = HintState.Idle
        _session.value = SessionState()
    }

    /** Creates a prose. Validation lives here so the UI cannot skip it. */
    fun createProverb(original: String, meaning: String, keywords: String) {
        val error = validateProverb(original, meaning)
        if (error != null) {
            _formResult.value = FormResult.Invalid(error)
            return
        }
        viewModelScope.launch {
            repository.addProverb(original, meaning, keywords)
            _formResult.value = FormResult.Saved
        }
    }

    fun editProverb(proverb: ProverbEntity, original: String, meaning: String, keywords: String) {
        val error = validateProverb(original, meaning)
        if (error != null) {
            _formResult.value = FormResult.Invalid(error)
            return
        }
        viewModelScope.launch {
            repository.updateProverb(
                proverb.copy(
                    original = original.trim(),
                    meaning = meaning.trim(),
                    keywords = keywords.trim()
                )
            )
            _formResult.value = FormResult.Saved
        }
    }

    fun deleteProverb(proverb: ProverbEntity) {
        viewModelScope.launch { repository.deleteProverb(proverb) }
    }

    fun deleteAttempt(attempt: AttemptEntity) {
        viewModelScope.launch { repository.deleteAttempt(attempt.id) }
    }

    fun clearArchive() {
        viewModelScope.launch { repository.clearAttempts() }
    }

    fun clearFormResult() {
        _formResult.value = null
    }

    private fun validateProverb(original: String, meaning: String): String? = when {
        original.isBlank() -> "Enter the proverb in the original language"
        meaning.isBlank() -> "Enter the meaning or translation"
        original.trim().length < MIN_PROVERB_LENGTH ->
            "A proverb needs at least $MIN_PROVERB_LENGTH characters"
        else -> null
    }

    private companion object {
        const val SESSION_SIZE = 5
        const val MIN_PROVERB_LENGTH = 6
    }
}
