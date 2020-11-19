package ru.countermeasure.wallpapershome.utils

import android.webkit.MimeTypeMap

object Util {

    fun getExtensionFromUrl(url: String): String {
        return url.substring(url.lastIndexOf('.') + 1)
//        return MimeTypeMap.getFileExtensionFromUrl(url)
    }
}