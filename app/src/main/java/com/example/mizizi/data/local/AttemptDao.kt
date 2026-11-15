package com.example.mizizi.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mizizi.data.model.AttemptEntity

@Dao
interface AttemptDao {

    /** Newest attempt first, so the archive reads like a log. */
    @Query("SELECT * FROM attempts ORDER BY createdAt DESC")
    fun observeAll(): LiveData<List<AttemptEntity>>

    @Query("SELECT COUNT(*) FROM attempts")
    suspend fun count(): Int

    @Insert
    suspend fun insert(attempt: AttemptEntity): Long

    @Query("DELETE FROM attempts WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM attempts")
    suspend fun clear()
}
