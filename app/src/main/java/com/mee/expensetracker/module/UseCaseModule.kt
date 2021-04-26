package com.mee.expensetracker.module

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Michelle Dayangco on 3/19/21.
 */

@Module(includes = arrayOf(DBModule::class, RepositoryModule::class))
@InstallIn(SingletonComponent::class)
class UseCaseModule {


    @Singleton
    @Provides
    @Named("Check_Network")
    fun providesNetworkChecking(@ApplicationContext context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true
                }
            }
        }
        return false
    }


    @Singleton
    @Provides
    fun providesDBSaveIncomeUseCase(appDatabase: AppDatabase,
                                  repository: FStoreIncomeRespository,
                                  @Named("Check_Network") hasNetwork: Boolean) = SaveIncomeUseCase(appDatabase, repository,hasNetwork)

    @Singleton
    @Provides
    fun providesSignInUseCase(repository: FAuthRepository) = SignInUseCase(repository)

    @Singleton
    @Provides
    fun providesDBSaveExpenseUseCase(appDatabase: AppDatabase,
                                     repository: FStoreExpenseRespository,
                                     @Named("Check_Network") hasNetwork: Boolean) = DBSaveExpenseUseCase(appDatabase, repository,hasNetwork)

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