package com.mee.expensetracker.db.dao

import androidx.room.*
import com.mee.expensetracker.model.Expense
import com.mee.expensetracker.model.Income
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by Michelle Dayangco on 3/21/21.
 */
@Dao
interface IncomeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(income: Income): Completable

    @Query("DELETE FROM Income")
    fun reset(): Completable

    @Query("SELECT * FROM Income WHERE id = :id")
    fun get(id: Int? = 0) : Single<Income>

    @Update
    fun update(income: Income): Completable

}