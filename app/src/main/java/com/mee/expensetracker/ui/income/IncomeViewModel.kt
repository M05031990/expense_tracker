package com.mee.expensetracker.ui.income

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mee.expensetracker.base.BaseViewModel
import com.mee.expensetracker.base.RequestResponse
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
class IncomeViewModel @Inject constructor(
    private val dbSaveIncomeUseCase: DBSaveIncomeUseCase,
    private val sharedPreferenceManager: SharedPreferenceManager
) : BaseViewModel() {
    var income: Income? = null
    private val _saveResponse: MutableLiveData<RequestResponse<Nothing>> = MutableLiveData()
    val saveResponse: LiveData<RequestResponse<Nothing>>
        get() {
            return _saveResponse
        }

    fun save(updatedIncome: Income) {

        dbSaveIncomeUseCase(updatedIncome).subscribe(this, _saveResponse,
            onComplete = Action {
                setIsInitialize()
            })

    }

    fun setIsInitialize() {
        if (!sharedPreferenceManager.isInitialize())
            sharedPreferenceManager.setInitialize()
    }
}