package ru.countermeasure.wallpapershome.domain.abstractions

import io.reactivex.Maybe
import io.reactivex.Observable
import ru.countermeasure.wallpapershome.domain.models.OperationStatus
import java.io.File
import java.io.InputStream

interface ImageCacheDataSource {
    fun saveImage(
        name: String,
        extension: String,
        inputStream: InputStream,
        contentLength: Long
    ): Observable<OperationStatus>

    fun getImageRx(name: String, extension: String): Maybe<File>

    fun clearCache()
}