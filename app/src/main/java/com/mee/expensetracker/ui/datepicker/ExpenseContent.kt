package com.carded.api

import androidx.lifecycle.LiveData
import com.mee.expensetracker.model.Expense
import java.util.*

sealed class ExpenseContent{
    data class Item( val expense: Expense): ExpenseContent()
    data class Header( val date: Date): ExpenseContent()
}
