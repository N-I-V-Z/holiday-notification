package com.nivz.holidaynotification.di

import android.content.Context
import androidx.room.Room
import com.nivz.holidaynotification.data.database.AppDatabase
import com.nivz.holidaynotification.data.database.HolidayDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for database dependency injection
 * Provides singleton instances of database and DAOs
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideHolidayDao(appDatabase: AppDatabase): HolidayDao {
        return appDatabase.holidayDao()
    }
}
