package com.mee.expensetracker.ui.expense

import com.mee.expensetracker.base.BaseViewModel
import com.mee.expensetracker.domain.DeleteExpenseUseCase
import com.mee.expensetracker.model.Expense
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.functions.Action
import javax.inject.Inject

/**
 * Created by Michelle Dayangco
 */
@HiltViewModel
class ExpenseViewModel @Inject constructor(private val deleteExpenseUseCase: DeleteExpenseUseCase): BaseViewModel() {


    fun delete(expense: Expense, onFinish: Action){
        deleteExpenseUseCase(expense).subscribe(this, onFinished = onFinish )
    }

}