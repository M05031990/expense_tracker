package com.mee.expensetracker.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carded.api.CategoryContent
import com.carded.api.ExpenseContent
import com.mee.expensetracker.base.BaseViewModel
import com.mee.expensetracker.db.SharedPreferenceManager
import com.mee.expensetracker.domain.GetExpensesUseCase
import com.mee.expensetracker.domain.GetIncomeUseCase
import com.mee.expensetracker.domain.SaveExpenseUseCase
import com.mee.expensetracker.model.Expense
import com.mee.expensetracker.model.FilterDateRange
import com.mee.expensetracker.model.Income
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import java.util.*
import javax.inject.Inject

/**
 * Created by Michelle Dayangco
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val saveExpenseUseCase: SaveExpenseUseCase,
                                        private val getExpensesUseCase: GetExpensesUseCase,
                                        private val  sharedPreferenceManager: SharedPreferenceManager,
                                        private val getIncomeUseCase: GetIncomeUseCase): BaseViewModel() {

    var expense: Expense? = null
    var expenseList: List<Expense> = arrayListOf()
    private var _expenseContents: MutableLiveData<List<ExpenseContent>>  = MutableLiveData()
    val expenseContents : LiveData<List<ExpenseContent>> get() { return _expenseContents}

    fun save(onSuccess: Action, onError: Action, onProgress: Consumer<Boolean>){
        expense?.let {
            saveExpenseUseCase(it).subscribe(this,
                onComplete = Action {
                    onSuccess.run()
                }, onError = Consumer {
                    onError.run()
                }, onProgress = onProgress)
        }
    }

    fun getAll(onError: Consumer<String>){
        getExpensesUseCase().subscribe(this,
        onSuccess = Consumer {
                expenseList = it
               parseExpenseContents(it)
        }, onError = onError)
    }
    fun getTotalExForMyDay(onTotal: Consumer<Double>){
        var total: Double = 0.00
        val exList = expenseList.filter { sameDay(it.create_at, Date())}
        exList.forEach {
            total = total + it.amount
        }
        onTotal.accept(total)
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

    private fun sameMonth(date1: Date, date2: Date): Boolean{
        if (date1.month == date2.month && date1.year == date2.year)
            return true
        return false
    }
    private fun sameYear(date1: Date, date2: Date): Boolean{
        if (date1.year == date2.year)
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
        getIncomeUseCase().subscribe(this, onProgress = onProgress,
        onSuccess = onSuccess, onError = onError)
    }

    fun isMyDayTurnOn(): Boolean{
        return sharedPreferenceManager.isMyDayTurnOn()
    }

    fun filterList(category: List<CategoryContent>, filterDateRange: FilterDateRange, onDone: Action){
        val list: MutableList<String> = mutableListOf()
        category.forEach {
            if (it is CategoryContent.Item)
                list.add(it.content)
        }
        val curDate = Date()
        val exList = expenseList.filter {
            val findCat = it.categories?.find { category->
                list.find { category.equals(it) } != null
            }

            val range = when(filterDateRange){
                FilterDateRange.TODAY -> sameDay(curDate,it.create_at)
                FilterDateRange.MONTH -> sameMonth(curDate,it.create_at)
                FilterDateRange.YEAR -> sameYear(curDate,it.create_at)
            }

            range || findCat != null
        }
        val contentList: MutableList<ExpenseContent> = mutableListOf()
        exList.forEach {
            contentList.add(ExpenseContent.Item(it))
        }
        _expenseContents.value = contentList

        onDone.run()

    }


}