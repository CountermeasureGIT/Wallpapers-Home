package ru.countermeasure.wallpapershome.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Maybe
import io.reactivex.Observable
import ru.countermeasure.wallpapershome.domain.abstractions.ImageCacheDataSource
import ru.countermeasure.wallpapershome.domain.models.OperationStatus
import java.io.File
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import kotlin.math.round

class ImageCacheDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ImageCacheDataSource {

    @Throws(IOException::class)
    override fun saveImage(
        name: String,
        extension: String,
        inputStream: InputStream,
        contentLength: Long
    ): Observable<OperationStatus> = Observable.create { emitter ->
        val tempFile = File(context.cacheDir, "$name.$extension")

        inputStream.use {
            try {
                tempFile.createNewFile()
                tempFile.outputStream().use { outputStream ->

                    var lastEmitterProgress = 0
                    var currentProgress = 0

                    val buffer = ByteArray(1024 * 8)
                    var contentLoaded = 0L
                    var readBytes: Int

                    while (true) {
                        currentProgress = calcProgress(contentLoaded, contentLength)
                        if (currentProgress - lastEmitterProgress >= 5) {
                            emitter.onNext(OperationStatus.InProgress(currentProgress))
                            lastEmitterProgress = currentProgress
                        }

                        readBytes = inputStream.read(buffer)
                        if (readBytes == -1)
                            break
                        outputStream.write(buffer, 0, readBytes)
                        contentLoaded += readBytes
                        if (emitter.isDisposed)
                            return@create
                    }
                }
                emitter.onNext(OperationStatus.Finished(tempFile))
                emitter.onComplete()
            } catch (e: IOException) {
                emitter.onError(e)
            }
        }
    }

    override fun getImageRx(name: String, extension: String) = Maybe.create<File> { emitter ->
        try {
            val imageFile = File(context.cacheDir, "$name.$extension")
            if (imageFile.exists() && imageFile.isFile)
                emitter.onSuccess(imageFile)
            emitter.onComplete()
        } catch (e: Throwable) {
            emitter.onError(e)
        }
    }

    override fun clearCache() {

    }

    private fun calcProgress(part: Long, total: Long): Int =
        (round(part.toFloat() * 100 / total)).toInt()

}