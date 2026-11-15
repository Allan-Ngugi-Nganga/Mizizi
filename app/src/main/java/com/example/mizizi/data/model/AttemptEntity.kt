package com.example.mizizi.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One recorded practice attempt.
 *
 * The proverb text is copied in rather than joined, so the archive still reads
 * correctly after the underlying proverb is edited or deleted.
 */
@Entity(tableName = "attempts")
data class AttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val proverbId: Long,
    val proverbOriginal: String,
    val userAnswer: String,
    val grade: String,
    val createdAt: Long = System.currentTimeMillis()
)
