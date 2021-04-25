package com.mee.expensetracker.module

import android.content.Context
import com.mee.expensetracker.db.AppDatabase
import com.mee.expensetracker.domain.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Michelle Dayangco on 3/19/21.
 */

@Module(includes = arrayOf(DBModule::class))
@InstallIn(SingletonComponent::class)
class UseCaseModule {


    @Singleton
    @Provides
    fun providesDBSaveUserUseCase(appDatabase: AppDatabase) = DBSaveIncomeUseCase(appDatabase)

    @Singleton
    @Provides
    fun providesDBSaveExpenseUseCase(appDatabase: AppDatabase) = DBSaveExpenseUseCase(appDatabase)

    @Singleton
    @Provides
    fun providesDBGetAllExpensesUseCase(appDatabase: AppDatabase) = DBGetAllExpensesUseCase(appDatabase)

    @Singleton
    @Provides
    fun providesDBDeleteExpenseUseCase(appDatabase: AppDatabase) = DBDeleteExpenseUseCase(appDatabase)

    @Singleton
    @Provides
    fun providesDBGetIncomeUseCase(appDatabase: AppDatabase) = DBGetIncomeUseCase(appDatabase)


}