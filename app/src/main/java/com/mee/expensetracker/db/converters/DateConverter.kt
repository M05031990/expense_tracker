package com.carded.db.converters

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Michelle Dayangco on 3/29/21.
 */
class DateConverter {

    @SuppressLint("SimpleDateFormat")
    @TypeConverter
    fun fromDate(date: Date?): String?{
        if (date == null)   return null

        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return format.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    @TypeConverter
    fun toDate(str: String?): Date?{
        if (str == null) {
            return Date()
        }
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return format.parse(str)
    }
}