package ru.countermeasure.wallpapershome.utils

import android.content.res.Resources
import androidx.annotation.StringRes

data class UiError(
    private val message: String?,
    @StringRes private val stringResourceId: Int = NO_MESSAGE
) {
    companion object {
        const val NO_MESSAGE = 0
    }

    fun getMessageText(resources: Resources): String {
        return message ?: resources.getString(stringResourceId)
    }
}