package com.mee.expensetracker.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mee.expensetracker.model.Income
import com.mee.expensetracker.model.IncomeType
import java.lang.reflect.Type

class IncomeTypeConverter{
        @TypeConverter
        fun fromSocialLinks(data: IncomeType): Int{
                return data.ordinal
        }

        @TypeConverter
        fun toSocialLinks(ordinal: Int): IncomeType{
                return IncomeType.getIncomeType(ordinal)
        }
}