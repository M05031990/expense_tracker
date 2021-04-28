package com.mee.expensetracker.domain

import android.content.Context
import com.mee.expensetracker.base.CompletableUseCaseWrapper
import com.mee.expensetracker.base.CompletableWrapper
import com.mee.expensetracker.db.AppDatabase
import com.mee.expensetracker.domain.repository.FStoreExpenseRespository
import com.mee.expensetracker.domain.repository.FStoreIncomeRespository
import com.mee.expensetracker.model.Expense
import com.mee.expensetracker.model.Income
import com.mee.expensetracker.util.AppUtils
import dagger.Reusable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by Michelle Dayangco on 3/17/21.
 */
class SaveExpenseUseCase @Inject constructor(private val context: Context,
                                             private val appDatabase: AppDatabase,
                                             private val repository: FStoreExpenseRespository): CompletableUseCaseWrapper() {

    operator fun invoke(expense: Expense): CompletableWrapper {
        if (AppUtils.checkNetwork(context))
            return buildUseCase {
                appDatabase.expenseDao().save(expense).andThen(repository.addExpenses(expense))
            }
        else  return saveToLocal(expense)
    }

    fun saveToLocal(expense: Expense): CompletableWrapper{
        return buildUseCase {
            appDatabase.expenseDao().save(expense)
        }
    }
}