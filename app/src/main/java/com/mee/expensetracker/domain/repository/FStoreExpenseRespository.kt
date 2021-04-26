package com.mee.expensetracker.domain.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mee.expensetracker.model.Expense
import com.mee.expensetracker.model.Income
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Michelle Dayangco
 */
class FStoreExpenseRespository @Inject constructor(private val collectionReference: CollectionReference, private val auth: FirebaseAuth) {

    fun addExpenses(expense: Expense): Completable{
        val id = auth.currentUser?.uid?:"user"
        if (expense.uid.isNullOrEmpty()){
            val newDoc = collectionReference.document()
            expense.uid = newDoc.id
            expense.user_uid = id
        }

       return Completable.create { emitter ->
             collectionReference.document(expense.uid).set(expense)
              .addOnSuccessListener {
                  emitter.onComplete()
              }
              .addOnFailureListener {
                  emitter.onError(it.fillInStackTrace())
              }
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}