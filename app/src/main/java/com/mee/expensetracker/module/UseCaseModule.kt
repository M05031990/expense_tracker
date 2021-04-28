package com.mee.expensetracker.module

import android.content.Context
import com.mee.expensetracker.db.AppDatabase
import com.mee.expensetracker.domain.*
import com.mee.expensetracker.domain.repository.FAuthRepository
import com.mee.expensetracker.domain.repository.FStoreExpenseRespository
import com.mee.expensetracker.domain.repository.FStoreIncomeRespository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Michelle Dayangco on 3/19/21.
 */

@Module(includes = arrayOf(DBModule::class, RepositoryModule::class))
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun providesDBSaveIncomeUseCase(@ApplicationContext context: Context,
                                    appDatabase: AppDatabase,
                                  repository: FStoreIncomeRespository) = SaveIncomeUseCase(context,appDatabase, repository)

    @Singleton
    @Provides
    fun providesSignInUseCase(repository: FAuthRepository) = SignInUseCase(repository)

    @Singleton
    @Provides
    fun providesDBSaveExpenseUseCase(@ApplicationContext context: Context,
                                     appDatabase: AppDatabase,
                                     repository: FStoreExpenseRespository) = SaveExpenseUseCase(context,appDatabase, repository)

    @Singleton
    @Provides
    fun providesDBGetAllExpensesUseCase(@ApplicationContext context: Context,
                                        appDatabase: AppDatabase,
                                        repository: FStoreExpenseRespository) = GetExpensesUseCase(context,appDatabase, repository)

    @Singleton
    @Provides
    fun providesDBDeleteExpenseUseCase(@ApplicationContext context: Context,
                                       appDatabase: AppDatabase,
                                       repository: FStoreExpenseRespository) = DeleteExpenseUseCase(context,appDatabase, repository)

    @Singleton
    @Provides
    fun providesDBGetIncomeUseCase(@ApplicationContext context: Context,
                                   appDatabase: AppDatabase,
                                   repository: FStoreIncomeRespository) = GetIncomeUseCase(context,appDatabase,repository)


}