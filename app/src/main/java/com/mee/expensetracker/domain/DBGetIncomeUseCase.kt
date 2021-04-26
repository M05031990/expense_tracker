package com.mee.expensetracker.domain

import com.mee.expensetracker.base.SingleUseCaseWrapper
import com.mee.expensetracker.base.SingleWrapper
import com.mee.expensetracker.db.AppDatabase
import com.mee.expensetracker.model.Expense
import com.mee.expensetracker.model.Income
import dagger.Reusable
import javax.inject.Inject

/**
 * Created by Michelle Dayangco on 3/17/21.
 */
class DBGetIncomeUseCase @Inject constructor(
        private val appDatabase: AppDatabase
): SingleUseCaseWrapper() {

    operator fun invoke(): SingleWrapper<Income> {
        return buildUseCase {
            appDatabase.incomeDao().get(0)
        }
    }
}