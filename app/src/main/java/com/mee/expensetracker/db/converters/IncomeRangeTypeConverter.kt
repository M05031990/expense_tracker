package com.mee.expensetracker.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mee.expensetracker.model.Income
import com.mee.expensetracker.model.IncomeTimeRange
import com.mee.expensetracker.model.IncomeType
import java.lang.reflect.Type

class IncomeRangeTypeConverter{
        @TypeConverter
        fun fromSocialLinks(data: IncomeTimeRange): Int{
                return data.ordinal
        }

        @TypeConverter
        fun toSocialLinks(ordinal: Int): IncomeTimeRange{
                return IncomeTimeRange.getType(ordinal)
        }
}