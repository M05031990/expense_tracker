package com.mee.expensetracker.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.mee.expensetracker.domain.repository.FAuthRepository
import com.mee.expensetracker.domain.repository.FStoreExpenseRespository
import com.mee.expensetracker.domain.repository.FStoreIncomeRespository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Michelle Dayangco
 */
@Module(includes = arrayOf(FirebaseModule::class))
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Singleton
    @Provides
    fun providesIncomeRespository(@Named("Collection_Income") collectionReference: CollectionReference,
    auth: FirebaseAuth) = FStoreIncomeRespository(collectionReference, auth)

    @Singleton
    @Provides
    fun providesExpenseRespository(@Named("Collection_Expenses") collectionReference: CollectionReference,
    auth: FirebaseAuth) = FStoreExpenseRespository(collectionReference, auth)

    @Singleton
    @Provides
    fun providesFAuthRepository(auth: FirebaseAuth) = FAuthRepository(auth)

}