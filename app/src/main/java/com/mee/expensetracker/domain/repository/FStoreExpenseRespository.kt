package com.mee.expensetracker.domain.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObject
import com.mee.expensetracker.model.Expense
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


/**
 * Created by Michelle Dayangco
 */
class FStoreExpenseRespository @Inject constructor(private val collectionReference: CollectionReference, private val auth: FirebaseAuth) {

    fun getUseID() = auth.currentUser?.uid?:"user"

    fun addExpenses(expense: Expense): Completable{

        if (expense.uid.isNullOrEmpty()){
            val newDoc = collectionReference.document()
            expense.uid = newDoc.id
            expense.user_uid = getUseID()
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

    fun getExpenses(): Single<List<Expense>>{
        return Single.create<List<Expense>> {emitter ->
            collectionReference.whereEqualTo("user_uid", getUseID())
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        val list: MutableList<Expense> = mutableListOf()
                         task.result?.forEach {
                             val expense = it.toObject<Expense>()
                             list.add(expense)
                         }
                        emitter.onSuccess(list)
                    }else{
                        task.exception?.fillInStackTrace()?.let { emitter.onError(it) }?:
                                emitter.onError(Throwable("Failed to get expenses in firestore."))
                    }

                }
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun deleteExpenses(expense: Expense): Completable{
        return Completable.create { emitter ->
            collectionReference.document(expense.uid).delete()
                .addOnSuccessListener {
                    emitter.onComplete()
                }
                .addOnFailureListener {
                    emitter.onError(it.fillInStackTrace())
                }
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}