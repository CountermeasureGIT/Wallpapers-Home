package ru.countermeasure.wallpapershome.domain.abstractions

import io.reactivex.Single
import okhttp3.ResponseBody

interface ImageRemoteDataSource {
    fun downloadImageRx(url: String): Single<ResponseBody>
}