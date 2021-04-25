package com.mee.expensetracker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.carded.db.converters.DateConverter
import com.mee.expensetracker.db.converters.IncomeRangeTypeConverter
import com.mee.expensetracker.db.converters.IncomeTypeConverter
import com.mee.expensetracker.db.converters.ListStringConverter
import com.mee.expensetracker.db.dao.ExpenseDao
import com.mee.expensetracker.db.dao.IncomeDao
import com.mee.expensetracker.model.Expense
import com.mee.expensetracker.model.Income

/**
 * Created by Michelle Dayangco on 3/21/21.
 */
@Database(entities = arrayOf( Income::class, Expense::class), version = AppDatabase.DB_VERSION)
@TypeConverters( DateConverter::class, ListStringConverter::class, IncomeTypeConverter::class, IncomeRangeTypeConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun incomeDao(): IncomeDao
    abstract fun expenseDao(): ExpenseDao
    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "Ex_Room_DB"
    }
}