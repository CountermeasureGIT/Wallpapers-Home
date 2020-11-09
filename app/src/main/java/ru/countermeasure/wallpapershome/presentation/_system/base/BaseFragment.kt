package ru.countermeasure.wallpapershome.presentation._system.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseFragment : Fragment() {
    abstract val layoutRes: Int

    private val renderDisposables = CompositeDisposable()

    protected fun Disposable.disposeOnPause() {
        renderDisposables.add(this)
    }

    protected fun disposeOnPause(vararg disposables: Disposable) {
        renderDisposables.addAll(*disposables)
    }

    override fun onPause() {
        super.onPause()
        renderDisposables.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutRes, container, false)
}