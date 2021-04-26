package com.mee.expensetracker.ui.income

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mee.expensetracker.base.BaseViewModel
import com.mee.expensetracker.base.RequestResponse
import com.mee.expensetracker.db.SharedPreferenceManager
import com.mee.expensetracker.domain.SaveIncomeUseCase
import com.mee.expensetracker.domain.SignInUseCase
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
    private val saveIncomeUseCase: SaveIncomeUseCase,
    private val sharedPreferenceManager: SharedPreferenceManager,
    private val signInUseCase: SignInUseCase) : BaseViewModel() {
    var income: Income? = null
    private val _saveResponse: MutableLiveData<RequestResponse<Nothing>> = MutableLiveData()
    val saveResponse: LiveData<RequestResponse<Nothing>>
        get() {
            return _saveResponse
        }

    fun save(updatedIncome: Income) {

        if (sharedPreferenceManager.isInitialize()){
            saveIncome(updatedIncome)
        }else{
            signInUseCase().subscribe(this, onComplete = Action {
                saveIncome(updatedIncome)
            })
        }


    }

    private fun saveIncome(updatedIncome: Income){
        saveIncomeUseCase(updatedIncome).subscribe(this, _saveResponse,
            onComplete = Action {
                setIsInitialize()
            }, onError = Consumer {
                _saveResponse.value = RequestResponse.Failure(it)
            })
    }

    fun setIsInitialize() {
        if (!sharedPreferenceManager.isInitialize())
            sharedPreferenceManager.setInitialize()
    }
}