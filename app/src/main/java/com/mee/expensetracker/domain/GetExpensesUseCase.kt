package com.mee.expensetracker.domain

import android.content.Context
import com.mee.expensetracker.base.SingleUseCaseWrapper
import com.mee.expensetracker.base.SingleWrapper
import com.mee.expensetracker.db.AppDatabase
import com.mee.expensetracker.domain.repository.FAuthRepository
import com.mee.expensetracker.domain.repository.FStoreExpenseRespository
import com.mee.expensetracker.model.Expense
import com.mee.expensetracker.model.Income
import com.mee.expensetracker.util.AppUtils
import dagger.Reusable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by Michelle Dayangco on 3/17/21.
 */
class GetExpensesUseCase @Inject constructor(@ApplicationContext private val context: Context,
                                             private val appDatabase: AppDatabase,
                                             private val repository: FStoreExpenseRespository): SingleUseCaseWrapper() {

    operator fun invoke(): SingleWrapper<List<Expense>> {
        if (AppUtils.checkNetwork(context)){
            return buildUseCase {
                repository.getExpenses()
            }
        }else return toLocal()
    }
    fun toLocal(): SingleWrapper<List<Expense>>{
        return buildUseCase {
            appDatabase.expenseDao().getAll()
        }
    }
}