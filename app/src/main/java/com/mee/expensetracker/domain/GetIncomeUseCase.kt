package com.mee.expensetracker.domain

import android.content.Context
import com.mee.expensetracker.base.SingleUseCaseWrapper
import com.mee.expensetracker.base.SingleWrapper
import com.mee.expensetracker.db.AppDatabase
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
class GetIncomeUseCase @Inject constructor(@ApplicationContext private val context: Context,
                                           private val appDatabase: AppDatabase,
                                           private val repository: FStoreIncomeRespository): SingleUseCaseWrapper() {

    operator fun invoke(): SingleWrapper<Income> {
        if (AppUtils.checkNetwork(context)){
            return buildUseCase {
                repository.getIncome()
            }
        }
        return toLocal()
    }

    fun toLocal():  SingleWrapper<Income>{
        return buildUseCase {
            appDatabase.incomeDao().get(0)
        }
    }

}