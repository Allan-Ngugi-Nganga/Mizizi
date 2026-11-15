package com.example.mizizi.domain

import com.example.mizizi.data.model.ProverbEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SessionEngineTest {

    private fun proverb(id: Long, original: String, meaning: String, keywords: String) =
        ProverbEntity(
            id = id,
            original = original,
            meaning = meaning,
            keywords = keywords,
            isSeed = true
        )

    private val proverbs = listOf(
        proverb(1, "Haraka haraka haina baraka", "Haste makes waste", "haste, hurry, waste, blessings"),
        proverb(2, "Mvumilivu hula mbivu", "The patient one eats ripe fruit", "patient, ripe, fruit"),
        proverb(3, "Penye nia pana njia", "Where there is a will, there is a way", "will, way, determination"),
        proverb(4, "Mtaka yote hukosa yote", "He who wants everything loses everything", "everything, wants, loses"),
        proverb(5, "Fadhili ni rehani", "A favour is a loan that must be repaid", "favour, loan, repay"),
        proverb(6, "Asiyekujua hakuthamini", "He who does not know you does not value you", "know, value, appreciate")
    )

    @Test
    fun `start opens a session of five questions`() {
        val engine = SessionEngine()
        val state = engine.start(proverbs)

        assertEquals(1, state.questionNumber)
        assertEquals(5, state.totalQuestions)
        assertEquals(0, state.score)
        assertNotNull(state.current)
        assertFalse(state.finished)
    }

    @Test
    fun `start on an empty collection finishes immediately`() {
        val engine = SessionEngine()
        val state = engine.start(emptyList())

        assertTrue(state.finished)
        assertEquals(0, state.totalQuestions)
        assertNull(state.current)
    }

    @Test
    fun `correct answer raises the score`() {
        val engine = SessionEngine()
        engine.start(listOf(proverbs.first()))
        val outcome = engine.submit("haste makes waste")

        assertNotNull(outcome)
        assertEquals(Grade.CORRECT, outcome!!.grade)
        assertEquals(1, engine.state.score)
    }

    @Test
    fun `close answer does not raise the score`() {
        val engine = SessionEngine()
        engine.start(listOf(proverbs.first()))
        val outcome = engine.submit("you should not hurry")

        assertEquals(Grade.CLOSE, outcome!!.grade)
        assertEquals(0, engine.state.score)
    }

    @Test
    fun `blank answer is incorrect and reports no keywords`() {
        val engine = SessionEngine()
        engine.start(listOf(proverbs.first()))
        val outcome = engine.submit("   ")

        assertEquals(Grade.INCORRECT, outcome!!.grade)
        assertTrue(outcome.matchedKeywords.isEmpty())
        assertEquals(0, engine.state.score)
    }

    @Test
    fun `next advances the question and the last one finishes the session`() {
        val engine = SessionEngine()
        engine.start(proverbs.take(2))
        assertEquals(1, engine.state.questionNumber)

        val second = engine.next()
        assertEquals(2, second.questionNumber)
        assertFalse(second.finished)

        val done = engine.next()
        assertTrue(done.finished)
        assertNull(done.current)
    }

    @Test
    fun `submit with no question in play returns null`() {
        val engine = SessionEngine()
        assertNull(engine.submit("anything"))
    }

    @Test
    fun `reset clears the session`() {
        val engine = SessionEngine()
        engine.start(proverbs)
        engine.submit("haste makes waste")

        val state = engine.reset()

        assertEquals(0, state.totalQuestions)
        assertEquals(0, state.score)
        assertFalse(state.isActive)
    }

    @Test
    fun `every proverb in a session is unique`() {
        val engine = SessionEngine(sessionSize = 4)
        val state = engine.start(proverbs)
        val seen = mutableListOf<ProverbEntity>()

        seen += state.current!!
        repeat(state.totalQuestions - 1) {
            seen += engine.next().current!!
        }

        assertEquals(4, seen.size)
        assertEquals(seen.distinctBy { it.id }.size, seen.size)
    }
}
