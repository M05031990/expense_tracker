package com.mee.expensetracker.domain

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.mee.expensetracker.base.CompletableUseCaseWrapper
import com.mee.expensetracker.base.CompletableWrapper
import com.mee.expensetracker.db.AppDatabase
import com.mee.expensetracker.domain.repository.FStoreIncomeRespository
import com.mee.expensetracker.model.Income
import com.mee.expensetracker.util.AppUtils
import dagger.Reusable
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Michelle Dayangco on 3/17/21.
 */
class SaveIncomeUseCase @Inject constructor(@ApplicationContext private val context: Context,
                                            private val appDatabase: AppDatabase,
                                            private val repository: FStoreIncomeRespository): CompletableUseCaseWrapper() {
    operator fun invoke(income: Income): CompletableWrapper {
          if (AppUtils.checkNetwork(context))
             return buildUseCase {
                 appDatabase.incomeDao().save(income)
                     .andThen(repository.setIncome(income))
             }
            else  return saveToLocal(income)
    }

    fun saveToLocal(income: Income): CompletableWrapper{
        return buildUseCase {
            appDatabase.incomeDao().reset().andThen(appDatabase.incomeDao().save(income))
        }
    }
}