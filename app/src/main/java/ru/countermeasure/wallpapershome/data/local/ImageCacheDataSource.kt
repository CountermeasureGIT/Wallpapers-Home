package ru.countermeasure.wallpapershome.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class ImageCacheDataSource @Inject constructor(@ApplicationContext context: Context) {

    fun saveImage(name: String, inputStream: InputStream) {
        TODO()
    }

    fun getImageOrNull(name: String): File? {
        TODO()
    }

    fun clearCache() {
        TODO()
    }

}