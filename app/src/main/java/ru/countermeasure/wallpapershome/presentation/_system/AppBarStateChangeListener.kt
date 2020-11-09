package ru.countermeasure.wallpapershome.presentation._system

import androidx.annotation.Px
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

abstract class AppBarScrimStateChangeListener(@Px private val scrimHeight: Int) :
    AppBarLayout.OnOffsetChangedListener {

    enum class State {
        VISIBLE, INVISIBLE
    }

    private var mCurrentState = State.INVISIBLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
        if (appBarLayout.totalScrollRange - abs(i) <= scrimHeight) {
            if (mCurrentState == State.INVISIBLE) {
                mCurrentState = State.VISIBLE
                onStateChanged(appBarLayout, mCurrentState)
            }
        } else {
            if (mCurrentState == State.VISIBLE) {
                mCurrentState = State.INVISIBLE
                onStateChanged(appBarLayout, mCurrentState)
            }
        }
    }

    abstract fun onStateChanged(
        appBarLayout: AppBarLayout?,
        state: State?
    )

}