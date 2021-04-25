package com.mee.expensetracker.db

import android.content.SharedPreferences
import dagger.Reusable
import javax.inject.Inject

/**
 * Created by Michelle Dayangco on 3/16/21.
 */
class SharedPreferenceManager @Inject constructor(private val sharedPref: SharedPreferences){
    private val KEY_INITIALIZE  = "KEY_INITIALIZE"
    private val KEY_MY_DAY  = "KEY_MY_DAY"

    fun setInitialize() {
        val editor = sharedPref.edit()
        editor.putBoolean(KEY_INITIALIZE, true)
        editor.apply()
    }

    fun isInitialize(): Boolean{
        return sharedPref.getBoolean(KEY_INITIALIZE, false)
    }

    fun turnOnMyDay(on: Boolean){
        val editor = sharedPref.edit()
        editor.putBoolean(KEY_MY_DAY, on)
        editor.apply()
    }
    fun isMyDayTurnOn(): Boolean{
        return sharedPref.getBoolean(KEY_MY_DAY, false)
    }
    fun clear(){
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }
}