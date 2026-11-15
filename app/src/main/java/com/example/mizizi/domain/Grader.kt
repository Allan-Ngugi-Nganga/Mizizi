package com.example.mizizi.domain

/** The three outcomes a translation attempt can receive. */
enum class Grade {
    CORRECT,
    CLOSE,
    INCORRECT;

    /** Human-friendly label used by the result screen. */
    fun label(): String = when (this) {
        CORRECT -> "Correct"
        CLOSE -> "Close"
        INCORRECT -> "Incorrect"
    }
}

/**
 * Pure Kotlin grading rules, so the logic can be unit tested without Android.
 *
 * The console prototype graded an attempt correct only when it contained every
 * keyword, which in practice meant almost nothing passed: four keywords for a
 * one-line meaning is a high bar. The app keeps the keyword idea but passes an
 * attempt once it hits the majority of them (at least two), so the feedback is
 * about understanding rather than word matching. A single hit is a hint.
 */
object Grader {

    /** Grades an attempt at translating [meaning] for a proverb with [keywords]. */
    fun grade(keywords: List<String>, meaning: String, answer: String): Grade {
        val cleaned = normalize(answer)
        if (cleaned.isEmpty()) return Grade.INCORRECT
        if (cleaned == normalize(meaning)) return Grade.CORRECT

        val hits = matchedKeywords(keywords, answer)
        val passMark = passMark(keywords.size)
        return when {
            hits.isNotEmpty() && hits.size >= passMark -> Grade.CORRECT
            hits.isNotEmpty() -> Grade.CLOSE
            else -> Grade.INCORRECT
        }
    }

    /** Half the keywords, rounded up, with a floor of two for short lists. */
    fun passMark(keywordCount: Int): Int = when {
        keywordCount <= 1 -> keywordCount
        else -> maxOf(2, kotlin.math.ceil(keywordCount / 2.0).toInt())
    }

    /** Every keyword that the attempt actually contains, in proverb order. */
    fun matchedKeywords(keywords: List<String>, answer: String): List<String> {
        val cleaned = normalize(answer)
        return keywords.filter { keyword -> normalize(keyword).let { it.isNotEmpty() && cleaned.contains(it) } }
    }

    /**
     * Lower-cases the text, drops punctuation and collapses whitespace so that
     * "Haste  makes, waste!" and "haste makes waste" compare equal.
     */
    fun normalize(text: String): String = text.lowercase()
        .replace(Regex("[^a-z0-9 ]"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()
}
