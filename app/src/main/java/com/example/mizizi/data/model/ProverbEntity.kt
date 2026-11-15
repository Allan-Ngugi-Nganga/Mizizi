package com.example.mizizi.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One proverb the user can practise.
 *
 * [keywords] is stored as a comma separated string because Room has no list
 * column type; [keywordList] turns it back into the list the grader wants.
 */
@Entity(tableName = "proverbs")
data class ProverbEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val original: String,
    val meaning: String,
    val keywords: String,
    val isSeed: Boolean = false
) {
    fun keywordList(): List<String> =
        keywords.split(",").map { it.trim() }.filter { it.isNotEmpty() }
}
