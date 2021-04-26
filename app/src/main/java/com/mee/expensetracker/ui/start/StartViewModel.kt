package com.mee.expensetracker.ui.start

import androidx.lifecycle.MutableLiveData
import com.mee.expensetracker.base.BaseViewModel
import com.mee.expensetracker.db.SharedPreferenceManager
import com.mee.expensetracker.domain.DBSaveIncomeUseCase
import com.mee.expensetracker.model.Income
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import javax.inject.Inject

/**
 * Created by Michelle Dayangco
 */
@HiltViewModel
class StartViewModel @Inject constructor(): BaseViewModel() {
}