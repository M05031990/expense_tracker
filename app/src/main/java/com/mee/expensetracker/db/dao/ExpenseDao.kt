package com.mee.expensetracker.db.dao

import androidx.room.*
import com.mee.expensetracker.model.Expense
import io.reactivex.Completable
import io.reactivex.Single


/**
 * Created by Michelle Dayangco on 4/21/21.
 */
@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(expense: Expense): Completable

    @Query("SELECT * FROM Expense  ORDER BY create_at DESC")
    fun getAll(): Single<List<Expense>>

    @Query("SELECT * FROM Expense WHERE id = :id")
    fun get(id: Int) : Single<Expense>

    @Query("SELECT COUNT(id) FROM Expense")
    fun getCount(): Single<Int>

    @Update
    fun update(expense: Expense): Completable

    @Query("DELETE FROM Expense  WHERE id = :id")
    fun delete(id: Int): Completable

    @Query("DELETE FROM Expense")
    fun clear():Completable
}