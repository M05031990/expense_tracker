package com.mee.expensetracker.domain

import com.mee.expensetracker.base.CompletableUseCaseWrapper
import com.mee.expensetracker.base.CompletableWrapper
import com.mee.expensetracker.db.AppDatabase
import com.mee.expensetracker.domain.repository.FStoreExpenseRespository
import com.mee.expensetracker.domain.repository.FStoreIncomeRespository
import com.mee.expensetracker.model.Expense
import com.mee.expensetracker.model.Income
import dagger.Reusable
import javax.inject.Inject

/**
 * Created by Michelle Dayangco on 3/17/21.
 */
class DBSaveExpenseUseCase @Inject constructor(private val appDatabase: AppDatabase,
                                               private val repository: FStoreExpenseRespository,
                                               private val hasNetwork: Boolean): CompletableUseCaseWrapper() {

    operator fun invoke(expense: Expense): CompletableWrapper {
        if (hasNetwork)
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