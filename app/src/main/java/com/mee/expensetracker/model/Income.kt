package com.mee.expensetracker.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * Created by Michelle Dayangco.
 */
@Entity
@Parcelize
data class Income (
    @PrimaryKey
    var id: Int = 0,
    var incomeType: IncomeType,
    var amount: Double,
    var range: IncomeTimeRange):Parcelable{

    constructor():this(0,IncomeType.SALARY,0.0, IncomeTimeRange.MONTHLY)

}