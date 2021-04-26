package com.mee.expensetracker.module

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mee.expensetracker.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Michelle Dayangco
 */
@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Singleton
    @Provides
    @Named("Collection_Income")
    fun provideCollectionIncome(firestore: FirebaseFirestore): CollectionReference {
        return firestore.collection("Income")
    }

    @Singleton
    @Provides
    @Named("Collection_Expenses")
    fun provideCollectionExpenses(firestore: FirebaseFirestore): CollectionReference {
        return firestore.collection("Expenses")
    }
}