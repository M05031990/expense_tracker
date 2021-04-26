package com.mee.expensetracker.domain

import com.mee.expensetracker.base.CompletableUseCaseWrapper
import com.mee.expensetracker.base.CompletableWrapper
import com.mee.expensetracker.db.AppDatabase
import com.mee.expensetracker.model.Income
import dagger.Reusable
import javax.inject.Inject

/**
 * Created by Michelle Dayangco on 3/17/21.
 */
class DBSaveIncomeUseCase @Inject constructor(
        private val appDatabase: AppDatabase
): CompletableUseCaseWrapper() {

    operator fun invoke(income: Income): CompletableWrapper {
        return buildUseCase {
            appDatabase.incomeDao().reset().andThen(appDatabase.incomeDao().save(income))
        }
    }
}