package com.nivz.holidaynotification.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nivz.holidaynotification.data.model.Holiday

/**
 * Room Database for storing holidays and events
 * Uses SQLite under the hood for efficient offline storage
 */
@Database(
    entities = [Holiday::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun holidayDao(): HolidayDao

    companion object {
        private const val DATABASE_NAME = "holiday_notification.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration() // For development; implement migrations for production
                .build()
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
