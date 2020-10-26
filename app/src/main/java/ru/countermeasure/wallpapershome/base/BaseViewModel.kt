package ru.countermeasure.wallpapershome.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel: ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    protected fun Disposable.collect() = compositeDisposable.add(this)

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}