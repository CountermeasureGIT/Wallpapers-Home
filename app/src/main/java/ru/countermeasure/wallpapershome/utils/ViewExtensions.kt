package ru.countermeasure.wallpapershome.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.screenWidth(): Int {
    val displayMetrics = DisplayMetrics()
    //TODO check out why its deprecated
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

fun runOnUiThread(action: Runnable) {
    Handler(Looper.getMainLooper()).post(action)
}