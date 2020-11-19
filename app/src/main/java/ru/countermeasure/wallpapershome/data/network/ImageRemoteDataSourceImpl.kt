package ru.countermeasure.wallpapershome.data.network

import io.reactivex.Single
import okhttp3.ResponseBody
import ru.countermeasure.wallpapershome.domain.abstractions.ImageRemoteDataSource
import ru.countermeasure.wallpapershome.utils.ServerError
import java.io.IOException
import javax.inject.Inject

class ImageRemoteDataSourceImpl @Inject constructor(
    private val wallheavenService: WallheavenService
) : ImageRemoteDataSource {

    @Throws(IOException::class, ServerError::class, RuntimeException::class)
    override fun downloadImageRx(url: String) = Single.create<ResponseBody> { emitter ->
        try {
            val response = wallheavenService.downloadImage(url).execute()
            if (!response.isSuccessful)
                throw ServerError(response.code().toString())

            val responseBody = response.body()
                ?: throw ServerError(
                    response.code().toString(),
                    "Null response body"
                )
            emitter.onSuccess(responseBody)
        } catch (e: Throwable) {
            emitter.onError(e)
        }
    }
}