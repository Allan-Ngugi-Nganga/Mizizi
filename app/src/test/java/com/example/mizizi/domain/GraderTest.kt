package com.example.mizizi.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class GraderTest {

    private val keywords = listOf("haste", "hurry", "waste", "blessings")
    private val meaning = "Haste makes waste"

    @Test
    fun `exact meaning is correct`() {
        assertEquals(Grade.CORRECT, Grader.grade(keywords, meaning, "Haste makes waste"))
    }

    @Test
    fun `punctuation and casing do not matter`() {
        assertEquals(Grade.CORRECT, Grader.grade(keywords, meaning, "  HASTE, makes waste!  "))
    }

    @Test
    fun `all keywords is correct even when worded differently`() {
        assertEquals(
            Grade.CORRECT,
            Grader.grade(keywords, meaning, "hurry leads to waste and lost blessings")
        )
    }

    @Test
    fun `majority of keywords is correct`() {
        assertEquals(
            Grade.CORRECT,
            Grader.grade(keywords, meaning, "waste and haste")
        )
    }

    @Test
    fun `single keyword hit is close`() {
        assertEquals(Grade.CLOSE, Grader.grade(keywords, meaning, "you should not hurry"))
    }

    @Test
    fun `no meaning words is incorrect`() {
        assertEquals(Grade.INCORRECT, Grader.grade(keywords, meaning, "the fish is fresh"))
    }

    @Test
    fun `blank answer is incorrect`() {
        assertEquals(Grade.INCORRECT, Grader.grade(keywords, meaning, "   "))
    }

    @Test
    fun `matched keywords are reported in proverb order`() {
        assertEquals(
            listOf("haste", "waste"),
            Grader.matchedKeywords(keywords, "waste and haste")
        )
    }

    @Test
    fun `normalize collapses spacing and drops punctuation`() {
        assertEquals("haraka haraka haina baraka", Grader.normalize(" Haraka,  haraka haina baraka. "))
    }
}
