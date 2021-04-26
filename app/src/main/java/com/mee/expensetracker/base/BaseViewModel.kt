package com.mee.expensetracker.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel(), CompositeDisposableContainer {

    val disposables = CompositeDisposable()

    protected var _progress: MutableLiveData<Boolean> = MutableLiveData()
    val progress: LiveData<Boolean> get() {return _progress}

    protected var _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String> get() {return _error}

    override fun getCompositeDisposable(): CompositeDisposable {
        return disposables
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}