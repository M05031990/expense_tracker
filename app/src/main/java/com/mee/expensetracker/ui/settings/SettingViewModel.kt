package com.mee.expensetracker.ui.settings

import com.mee.expensetracker.base.BaseViewModel
import com.mee.expensetracker.db.SharedPreferenceManager
import com.mee.expensetracker.domain.GetExpensesUseCase
import com.mee.expensetracker.domain.GetIncomeUseCase
import com.mee.expensetracker.model.Expense
import com.mee.expensetracker.model.Income
import com.mee.expensetracker.model.IncomeTimeRange
import com.mee.expensetracker.ui.datepicker.DateTimeDialogFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.functions.Consumer
import java.util.*
import javax.inject.Inject

/**
 * Created by Michelle Dayangco
 */
@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase,
    private val getIncomeUseCase: GetIncomeUseCase,
    private val sharedPreferenceManager: SharedPreferenceManager) : BaseViewModel() {

    var income: Income? = null
    fun getIncome(
        onSuccess: Consumer<Income>,
        onProgress: Consumer<Boolean>,
        onError: Consumer<String>
    ) {
        getIncomeUseCase().subscribe(this, onProgress = onProgress,
            onSuccess = Consumer {
                income = it
                onSuccess.accept(it)
            }, onError = onError
        )
    }
    fun getTotalExpense(onTotal: Consumer<Double>, onError: Consumer<String>) {
        getExpensesUseCase().subscribe(this,
            onSuccess = Consumer {
                onTotal.accept(getTotal(it))
            }, onError = onError
        )
    }

    fun getCurrentCalculatedTitle(): String {
        income?.let { income ->
            when (income.range) {
                IncomeTimeRange.MONTHLY -> {
                    return DateTimeDialogFragment.formateDateToMonthYear(Date())
                }
                IncomeTimeRange.YEARLY -> {
                    return DateTimeDialogFragment.formateDateToYear(Date())
                }
            }
        }

        return ""

    }

    private fun getTotal(list: List<Expense>): Double {
        var total: Double = 0.00
        income?.let { income ->
            val curDate = Date()
            list.forEach {
                when (income.range) {
                    IncomeTimeRange.MONTHLY -> {
                        if (it.create_at.month == curDate.month && it.create_at.year == curDate.year)
                            total = total + it.amount
                    }
                    IncomeTimeRange.YEARLY -> {
                        if (it.create_at.year == curDate.year)
                            total = total + it.amount
                    }
                }
            }
        }
        return total
    }

    fun saveSwitchMyDay(check: Boolean){
        sharedPreferenceManager.turnOnMyDay(check)
    }
    fun isMyDayTurnOn(): Boolean{
        return sharedPreferenceManager.isMyDayTurnOn()
    }
}