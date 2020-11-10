package ru.countermeasure.wallpapershome.data.repositories

import io.reactivex.Single
import ru.countermeasure.wallpapershome.data.local.ImageCacheDataSource
import ru.countermeasure.wallpapershome.data.network.WallheavenService
import ru.countermeasure.wallpapershome.domain.abstractions.WallpapersRepository
import java.io.File
import javax.inject.Inject

class WallpapersRepositoryImpl @Inject constructor(
    private val remoteDataSource: WallheavenService,
    private val cacheDataSource: ImageCacheDataSource
) : WallpapersRepository {
    override fun getImageFile(id: String, url: String): Single<File> = Single.defer {
        try {
            var resultImageFile: File? = cacheDataSource.getImageOrNull(id)
            if (resultImageFile == null) {
                remoteDataSource.downloadImage(url)
            }

            return@defer Single.just(resultImageFile)
        } catch (e: Exception) {
            return@defer Single.error<File>(e)
        }
    }
}