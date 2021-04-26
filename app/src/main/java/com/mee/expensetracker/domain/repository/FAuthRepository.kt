package com.mee.expensetracker.domain.repository

import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Michelle Dayangco
 */
class FAuthRepository @Inject constructor(private val auth: FirebaseAuth) {

    fun signin(): Completable{
        return Completable.create { emitter ->
            auth.signInAnonymously().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    emitter.onComplete()
                } else {
                    task.exception?.fillInStackTrace()?.let { emitter.onError(it) }
                }
            }
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    }
}