package com.mee.expensetracker.model

/**
 * Created by Michelle Dayangco.
 */
enum class IncomeType {
    SALARY ("Salary"),
    BUSINESS("Business"),
    EXTRAS("Extra");

    val title: String
    constructor(title: String){
        this.title = title
    }

    companion object{

        fun getIncomeType(order: Int): IncomeType{
            values().forEach {
                if (it.ordinal == order)
                    return it
            }
            return SALARY
        }

        fun getIncomeType(title: String): IncomeType{
            values().forEach {
                if (it.title == title)
                    return it
            }
            return SALARY
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