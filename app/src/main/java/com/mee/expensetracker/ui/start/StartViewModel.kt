package com.mee.expensetracker.ui.start

import com.mee.expensetracker.base.BaseViewModel
import com.mee.expensetracker.db.SharedPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Michelle Dayangco
 */
@HiltViewModel
class StartViewModel @Inject constructor(private val sharedPreferenceManager: SharedPreferenceManager): BaseViewModel() {

    fun isInit() = sharedPreferenceManager.isInitialize()
}