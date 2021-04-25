package com.mee.expensetracker.domain

import com.carded.api.*
import com.mee.expensetracker.db.AppDatabase
import com.mee.expensetracker.model.Expense
import com.mee.expensetracker.model.Income
import dagger.Reusable
import javax.inject.Inject

/**
 * Created by Michelle Dayangco on 3/17/21.
 */
class DBGetAllExpensesUseCase @Inject constructor(
        private val appDatabase: AppDatabase
): SingleUseCaseWrapper() {

    operator fun invoke(): SingleWrapper<List<Expense>> {
        return buildUseCase {
            appDatabase.expenseDao().getAll()
        }
    }
}