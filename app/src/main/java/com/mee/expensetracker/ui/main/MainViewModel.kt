package com.mee.expensetracker.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carded.api.ExpenseContent
import com.mee.expensetracker.base.BaseViewModel
import com.mee.expensetracker.db.SharedPreferenceManager
import com.mee.expensetracker.domain.DBGetAllExpensesUseCase
import com.mee.expensetracker.domain.DBGetIncomeUseCase
import com.mee.expensetracker.domain.DBSaveExpenseUseCase
import com.mee.expensetracker.model.Expense
import com.mee.expensetracker.model.Income
import com.mee.expensetracker.model.IncomeTimeRange
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import java.util.*
import javax.inject.Inject

/**
 * Created by Michelle Dayangco
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val dbSaveExpenseUseCase: DBSaveExpenseUseCase,
                                        private val dbGetAllExpensesUseCase: DBGetAllExpensesUseCase,
                                        private val  sharedPreferenceManager: SharedPreferenceManager,
                                        private val dbGetIncomeUseCase: DBGetIncomeUseCase): BaseViewModel() {

    var expense: Expense? = null
    var expenseList: List<Expense> = arrayListOf()
    private var _expenseContents: MutableLiveData<List<ExpenseContent>>  = MutableLiveData()
    val expenseContents : LiveData<List<ExpenseContent>> get() { return _expenseContents}

    fun save(onSuccess: Action, onError: Action, onProgress: Consumer<Boolean>){
        expense?.let {
            dbSaveExpenseUseCase(it).subscribe(this,
                onComplete = Action {
                    onSuccess.run()
                }, onError = Consumer {
                    onError.run()
                }, onProgress = onProgress)
        }
    }

    fun getAll(onError: Consumer<String>){
        dbGetAllExpensesUseCase().subscribe(this,
        onSuccess = Consumer {
                expenseList = it
               parseExpenseContents(it)
        }, onError = onError)
    }

    fun parseExpenseContentForMyDay(onTotal: Consumer<Double>){
        val contentList: MutableList<ExpenseContent> = mutableListOf()
        var total: Double = 0.00
        val exList = expenseList.filter { sameDay(it.create_at, Date())}
        exList.forEach {
            contentList.add(ExpenseContent.Item(it))
            total = total + it.amount
        }
        onTotal.accept(total)
        _expenseContents.value = contentList
    }
    private fun parseExpenseContents(list: List<Expense>){
        val contentList: MutableList<ExpenseContent> = mutableListOf()
        val allDates = getAllDates(list)
        allDates.forEach {date ->
            contentList.add(ExpenseContent.Header(date))
            val exList = list.filter { sameDay(it.create_at, date)}
            exList.forEach {
                contentList.add(ExpenseContent.Item(it))
            }
        }

        _expenseContents.value = contentList
    }

    private fun sameDay(date1: Date, date2: Date): Boolean{
        if (date1.day == date2.day && date1.month == date2.month && date1.year == date2.year)
            return true
        return false
    }

   private fun getAllDates(list: List<Expense>):List<Date>{
        val allDates: MutableList<Date> = mutableListOf()

        list.forEachIndexed { index, expense ->
            if (!allDates.any { sameDay(it, expense.create_at) }){
               allDates.add(expense.create_at)
            }
        }

        return allDates
    }

    fun getIncome(onSuccess: Consumer<Income>, onProgress: Consumer<Boolean>, onError: Consumer<String>){
        dbGetIncomeUseCase().subscribe(this, onProgress = onProgress,
        onSuccess = onSuccess, onError = onError)
    }

    fun isMyDayTurnOn(): Boolean{
        return sharedPreferenceManager.isMyDayTurnOn()
    }


}