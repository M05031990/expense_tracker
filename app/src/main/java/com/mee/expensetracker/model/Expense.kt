package com.mee.expensetracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Created by Michelle Dayangco.
 */
@Entity
data class Expense (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var uid: String,
    var user_uid: String,
    var title: String,
    var amount: Double,
    var create_at: Date,
    var categories: List<String>? = arrayListOf(),
    var description: String,
    var isMyDay: Boolean? = true){

    constructor(): this(0,"","","",0.0,Date(), arrayListOf(),"")
}