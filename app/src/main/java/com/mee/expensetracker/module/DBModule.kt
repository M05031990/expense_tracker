package com.mee.expensetracker.module

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room.databaseBuilder
import com.mee.expensetracker.db.AppDatabase
import com.mee.expensetracker.db.SharedPreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


/**
 * Created by Michelle Dayangco on 3/16/21.
 */

@Module
@InstallIn(SingletonComponent::class)
class DBModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return databaseBuilder(
                context, AppDatabase::class.java, AppDatabase.DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun provideSharePreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("Shared_ExTracker", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideSharePreferenceManager(sharedPreferences: SharedPreferences) =
        SharedPreferenceManager(sharedPreferences)
}