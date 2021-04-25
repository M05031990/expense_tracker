package com.mee.expensetracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Michelle Dayangco.
 */
@Entity
data class Income (
    @PrimaryKey
    var id: Int = 0,
    var incomeType: IncomeType,
    var amount: Double,
    var range: IncomeTimeRange){

    constructor():this(0,IncomeType.SALARY,0.0, IncomeTimeRange.MONTHLY)

}