package ru.countermeasure.wallpapershome.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.screenWidth(): Int {
    val displayMetrics = DisplayMetrics()
    requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

fun Fragment.hideKeyboard() {
    activity?.apply {
        currentFocus?.apply {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}

fun showKeyboard(focusedView: View, forced: Boolean = false) {
    focusedView.requestFocus()
    ContextCompat.getSystemService(focusedView.context, InputMethodManager::class.java)
        ?.showSoftInput(
            focusedView,
            if (forced) InputMethodManager.SHOW_FORCED else InputMethodManager.SHOW_IMPLICIT
        )
}