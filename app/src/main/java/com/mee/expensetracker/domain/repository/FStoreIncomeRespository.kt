package com.mee.expensetracker.domain.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.toObject
import com.google.gson.Gson
import com.mee.expensetracker.model.Expense
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

    fun getUseID() = auth.currentUser?.uid?:"user"

    fun setIncome(income: Income): Completable{
        documentReference = reference.document(getUseID())
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

    fun getIncome(): Single<Income>{
        documentReference = reference.document(getUseID())
        return Single.create<Income> { emitter ->
            documentReference.get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        task.result?.let {
                            if (it.exists()){
                                val income = it.toObject<Income>()
                                income?.let { it1 -> emitter.onSuccess(it1) }
                            }else
                                emitter.onError(Throwable("No income saved."))
                        }?:   emitter.onError(Throwable("No income saved."))
                    }else{
                        task.exception?.fillInStackTrace()?.let { emitter.onError(it) }?:
                        emitter.onError(Throwable("Failed to get income in firestore."))
                    }

                }
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}