package com.mee.expensetracker.model

/**
 * Created by Michelle Dayangco.
 */
enum class IncomeTimeRange {
    MONTHLY("Monthly"),
    YEARLY("Yearly");

    val title: String
    constructor(title: String){
        this.title = title
    }

    companion object{

        fun getType(order: Int): IncomeTimeRange{
            values().forEach {
                if (it.ordinal == order)
                    return it
            }
            return MONTHLY
        }

        fun getType(title: String): IncomeTimeRange{
            values().forEach {
                if (it.title == title)
                    return it
            }
            return MONTHLY
        }

        fun getList(): List<String>{
            val list: MutableList<String> = mutableListOf()
            values().forEach {
                list.add(it.title)
            }
            return list
        }
    }
}