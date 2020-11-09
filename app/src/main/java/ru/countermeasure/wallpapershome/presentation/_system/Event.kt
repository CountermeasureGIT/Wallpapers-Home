package ru.countermeasure.wallpapershome.presentation._system

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}

fun <T> Observable<Event<T>>.subscribeToEvent(onNext: (T) -> Unit): Disposable {
    return subscribe { event ->
        event.getContentIfNotHandled()?.let { onNext(it) }
    }
}

fun <T> BehaviorRelay<Event<T>>.acceptEvent(content: T) {
    accept(Event(content))
}