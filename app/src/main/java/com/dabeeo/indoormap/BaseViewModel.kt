package com.dabeeo.indoormap

import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {


    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        this.init()
    }

    abstract fun init()

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }


    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }


}
