package com.mee.expensetracker.domain.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.mee.expensetracker.model.Income
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Michelle Dayangco
 */
class FStoreIncomeRespository @Inject constructor(private val reference: CollectionReference, private val auth: FirebaseAuth) {
    lateinit var  documentReference: DocumentReference
    fun setIncome(income: Income): Completable{
        val id = auth.currentUser?.uid?:"user"
        documentReference = reference.document(id)
       return Completable.create { emitter ->
          documentReference.set(income)
              .addOnSuccessListener {
                  emitter.onComplete()
              }
              .addOnFailureListener {
                  emitter.onError(it.fillInStackTrace())
              }
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}