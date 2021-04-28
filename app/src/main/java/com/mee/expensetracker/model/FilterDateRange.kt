package com.mee.expensetracker.model

/**
 * Created by Michelle Dayangco.
 */
enum class FilterDateRange {
    TODAY("Today"),
    MONTH("Month"),
    YEAR("Year");

    val title: String
    constructor(title: String){
        this.title = title
    }

    companion object{

        fun getType(order: Int): FilterDateRange{
            values().forEach {
                if (it.ordinal == order)
                    return it
            }
            return TODAY
        }

        fun getType(title: String): FilterDateRange{
            values().forEach {
                if (it.title == title)
                    return it
            }
            return TODAY
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