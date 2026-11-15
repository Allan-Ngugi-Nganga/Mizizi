package com.example.mizizi.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

/** Result of a network call: either a value or a message safe to show the user. */
sealed class ApiResult<out T> {
    data class Success<T>(val value: T) : ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>()
}

/**
 * MyMemory's free translation endpoint.
 *
 * The app uses it to show a literal machine translation next to the user's own
 * attempt, which is exactly the contrast Mizizi is about: the machine matches
 * words, the learner has to show the meaning.
 */
class TranslationApi(
    private val endpoint: String = "https://api.mymemory.translated.net/get"
) {

    suspend fun swahiliToEnglish(text: String): ApiResult<String> = withContext(Dispatchers.IO) {
        if (text.isBlank()) return@withContext ApiResult.Error("Nothing to translate")

        var connection: HttpURLConnection? = null
        try {
            val query = URLEncoder.encode(text, "UTF-8")
            val url = URL("$endpoint?q=$query&langpair=sw%7Cen")
            connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = TIMEOUT_MS
                readTimeout = TIMEOUT_MS
                setRequestProperty("Accept", "application/json")
            }

            val code = connection.responseCode
            if (code !in 200..299) {
                ApiResult.Error("Translation service returned $code")
            } else {
                val body = connection.inputStream.bufferedReader().use { it.readText() }
                val translated = JSONObject(body)
                    .optJSONObject("responseData")
                    ?.optString("translatedText")
                    .orEmpty()
                    .trim()

                when {
                    translated.isEmpty() -> ApiResult.Error("The translation came back empty")
                    translated.contains("QUERY LENGTH LIMIT", ignoreCase = true) ->
                        ApiResult.Error("Translation limit reached, try again later")
                    else -> ApiResult.Success(translated)
                }
            }
        } catch (error: Exception) {
            ApiResult.Error(error.message ?: "Could not reach the translation service")
        } finally {
            connection?.disconnect()
        }
    }

    private companion object {
        const val TIMEOUT_MS = 8_000
    }
}
