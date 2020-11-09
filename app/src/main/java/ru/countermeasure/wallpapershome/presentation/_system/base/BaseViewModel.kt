package ru.countermeasure.wallpapershome.presentation._system.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    /**Collect disposable into [compositeDisposable] scoped to this ViewModel instance.
     * It will be disposed in [onCleared] callback*/
    protected fun Disposable.collect() = compositeDisposable.add(this)

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}