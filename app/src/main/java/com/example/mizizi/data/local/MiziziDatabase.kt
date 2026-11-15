package com.example.mizizi.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mizizi.data.model.AttemptEntity
import com.example.mizizi.data.model.ProverbEntity

/**
 * The app's single Room database.
 *
 * The seed rows are inserted straight into the SQLite file in [onCreate], which
 * Room runs only when the database is created for the first time, so editing or
 * deleting a seed proverb later is not undone on the next launch.
 */
@Database(
    entities = [ProverbEntity::class, AttemptEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MiziziDatabase : RoomDatabase() {

    abstract fun proverbDao(): ProverbDao

    abstract fun attemptDao(): AttemptDao

    companion object {
        private const val DATABASE_NAME = "mizizi.db"

        @Volatile
        private var instance: MiziziDatabase? = null

        fun getDatabase(context: Context): MiziziDatabase =
            instance ?: synchronized(this) {
                instance ?: build(context.applicationContext).also { instance = it }
            }

        private fun build(context: Context): MiziziDatabase =
            Room.databaseBuilder(context, MiziziDatabase::class.java, DATABASE_NAME)
                .addCallback(SeedCallback())
                .build()

        private class SeedCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                SeedData.proverbs.forEach { seed ->
                    db.execSQL(
                        "INSERT INTO proverbs (original, meaning, keywords, isSeed) VALUES (?, ?, ?, 1)",
                        arrayOf(seed.original, seed.meaning, seed.keywords)
                    )
                }
            }
        }
    }
}
