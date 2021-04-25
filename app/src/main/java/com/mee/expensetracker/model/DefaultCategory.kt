package com.mee.expensetracker.model

/**
 * Created by Michelle Dayangco.
 */
enum class DefaultCategory {
    FOOD("Food"),
    CLOTHING("Clothing"),
    GADGET("Gadget"),
    COMPUTER("Computer"),
    APPLIANCE("Appliance"),
    SPORTS("Sport"),
    BILL("Bill"),
    MOVIE("Movie"),
    ONLINE_SHOPPING("Online Shopping"),
    GROCERY("Grocery");

    var title: String
    constructor(title: String){
        this.title = title
    }
    companion object{

        fun getIncomeType(order: Int): DefaultCategory{
            values().forEach {
                if (it.ordinal == order)
                    return it
            }
            return FOOD
        }

        fun getIncomeType(title: String): DefaultCategory{
            values().forEach {
                if (it.title == title)
                    return it
            }
            return FOOD
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