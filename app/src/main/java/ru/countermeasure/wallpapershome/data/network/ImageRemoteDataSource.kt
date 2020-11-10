package ru.countermeasure.wallpapershome.data.network

import java.io.InputStream
import javax.inject.Inject

class ImageRemoteDataSource @Inject constructor(
    private val wallheavenService: WallheavenService
) {
    fun downloadImage(url: String): InputStream {
        val response = wallheavenService.downloadImage(url).execute()
        TODO()
    }
}