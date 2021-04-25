package com.mee.expensetracker.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ListStringConverter{
        @TypeConverter
        fun fromSocialLinks(list: List<String>?): String?{
                if (list == null)   return null
                val gson = Gson()
                val type: Type = object : TypeToken<List<String>?>() {}.type
                return gson.toJson(list, type)
        }

        @TypeConverter
        fun toSocialLinks(str: String?): List<String>?{
                if (str == null) {
                        return arrayListOf()
                }
                val gson = Gson()
                val type = object : TypeToken<List<String>?>() {}.type
                return gson.fromJson(str, type)
        }
}