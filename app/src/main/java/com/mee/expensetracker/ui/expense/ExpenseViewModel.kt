package com.mee.expensetracker.ui.expense

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carded.api.ExpenseContent
import com.mee.expensetracker.base.BaseViewModel
import com.mee.expensetracker.domain.DBDeleteExpenseUseCase
import com.mee.expensetracker.domain.DBGetAllExpensesUseCase
import com.mee.expensetracker.domain.DBSaveExpenseUseCase
import com.mee.expensetracker.model.Expense
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import java.util.*
import javax.inject.Inject

/**
 * Created by Michelle Dayangco
 */
@HiltViewModel
class ExpenseViewModel @Inject constructor(private val dbDeleteExpenseUseCase: DBDeleteExpenseUseCase): BaseViewModel() {


    fun delete(id: Int, onFinish: Action){
        dbDeleteExpenseUseCase(id).subscribe(this, onFinished = onFinish )
    }

}