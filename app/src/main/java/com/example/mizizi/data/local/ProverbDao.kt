package com.example.mizizi.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mizizi.data.model.ProverbEntity

@Dao
interface ProverbDao {

    /** The whole collection, re-emitted by Room whenever the table changes. */
    @Query("SELECT * FROM proverbs ORDER BY original COLLATE NOCASE ASC")
    fun observeAll(): LiveData<List<ProverbEntity>>

    @Query("SELECT * FROM proverbs WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): ProverbEntity?

    @Query("SELECT COUNT(*) FROM proverbs")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(proverb: ProverbEntity): Long

    @Update
    suspend fun update(proverb: ProverbEntity)

    @Delete
    suspend fun delete(proverb: ProverbEntity)
}
