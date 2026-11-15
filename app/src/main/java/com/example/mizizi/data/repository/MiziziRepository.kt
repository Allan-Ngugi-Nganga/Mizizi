package com.example.mizizi.data.repository

import androidx.lifecycle.LiveData
import com.example.mizizi.data.local.AttemptDao
import com.example.mizizi.data.local.ProverbDao
import com.example.mizizi.data.model.AttemptEntity
import com.example.mizizi.data.model.ProverbEntity
import com.example.mizizi.data.remote.ApiResult
import com.example.mizizi.data.remote.TranslationApi

/**
 * The only door into the data layer.
 *
 * The UI and the ViewModel never touch Room or the network directly: they call
 * this class, which decides whether a request goes to the database or to the
 * translation API.
 */
class MiziziRepository(
    private val proverbDao: ProverbDao,
    private val attemptDao: AttemptDao,
    private val translationApi: TranslationApi = TranslationApi()
) {

    val proverbs: LiveData<List<ProverbEntity>> = proverbDao.observeAll()

    val attempts: LiveData<List<AttemptEntity>> = attemptDao.observeAll()

    suspend fun proverbById(id: Long): ProverbEntity? = proverbDao.findById(id)

    suspend fun proverbCount(): Int = proverbDao.count()

    suspend fun attemptCount(): Int = attemptDao.count()

    suspend fun addProverb(original: String, meaning: String, keywords: String): Long =
        proverbDao.insert(
            ProverbEntity(
                original = original.trim(),
                meaning = meaning.trim(),
                keywords = keywords.trim(),
                isSeed = false
            )
        )

    suspend fun updateProverb(proverb: ProverbEntity) = proverbDao.update(proverb)

    suspend fun deleteProverb(proverb: ProverbEntity) = proverbDao.delete(proverb)

    suspend fun recordAttempt(
        proverb: ProverbEntity,
        answer: String,
        grade: String
    ): Long = attemptDao.insert(
        AttemptEntity(
            proverbId = proverb.id,
            proverbOriginal = proverb.original,
            userAnswer = answer.trim(),
            grade = grade
        )
    )

    suspend fun deleteAttempt(id: Long) = attemptDao.deleteById(id)

    suspend fun clearAttempts() = attemptDao.clear()

    /** A literal machine translation, used as a hint when grading goes wrong. */
    suspend fun literalTranslation(original: String): ApiResult<String> =
        translationApi.swahiliToEnglish(original)
}
