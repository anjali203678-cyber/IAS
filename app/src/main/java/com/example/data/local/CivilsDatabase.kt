package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        OfflineChapterEntity::class,
        LocalChapterProgressEntity::class,
        LocalStudyTaskEntity::class,
        LocalFlashcardEntity::class,
        LocalQuoteHistoryEntity::class,
        LocalTestAttemptEntity::class,
        LocalReminderEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CivilsDatabase : RoomDatabase() {
    abstract fun civilsDao(): CivilsDao

    companion object {
        @Volatile
        private var INSTANCE: CivilsDatabase? = null

        fun getInstance(context: Context): CivilsDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CivilsDatabase::class.java,
                    "civils_ias_offline.db"
                ).build().also { INSTANCE = it }
            }
    }
}
