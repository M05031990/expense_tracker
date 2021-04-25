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
class StartViewModel @Inject constructor(private val dbSaveIncomeUseCase: DBSaveIncomeUseCase,
                                         private val sharedPreferenceManager: SharedPreferenceManager): BaseViewModel() {
    var income: Income? = null
    fun save(onSuccess: Action, onError: Action, onProgress: Consumer<Boolean>){
        income?.let {
            dbSaveIncomeUseCase(it).subscribe(this,
                onComplete = Action {
                    sharedPreferenceManager.setInitialize()
                    onSuccess.run()
                }, onError = Consumer {
                    onError.run()
                }, onProgress = onProgress)
        }
    }
}