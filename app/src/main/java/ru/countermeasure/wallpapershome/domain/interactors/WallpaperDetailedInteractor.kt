package ru.countermeasure.wallpapershome.domain.interactors

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import ru.countermeasure.wallpapershome.domain.abstractions.ImageCacheDataSource
import ru.countermeasure.wallpapershome.domain.abstractions.ImageRemoteDataSource
import ru.countermeasure.wallpapershome.domain.models.ListWallpaper
import ru.countermeasure.wallpapershome.domain.models.OperationStatus
import ru.countermeasure.wallpapershome.utils.Util
import java.io.File
import javax.inject.Inject

class WallpaperDetailedInteractor @Inject constructor(
    private val remoteDataSource: ImageRemoteDataSource,
    private val cacheDataSource: ImageCacheDataSource
) {

    fun getImage(listWallpaper: ListWallpaper): Flowable<OperationStatus> =
        cacheDataSource.getImageRx(listWallpaper.id, Util.getExtensionFromUrl(listWallpaper.path))
            .map {
                OperationStatus.Finished<File>(it) as OperationStatus
            }
            .toObservable()
            .switchIfEmpty(
                remoteDataSource.downloadImageRx(listWallpaper.path)
                    .flatMapObservable {
                        cacheDataSource.saveImage(
                            listWallpaper.id,
                            Util.getExtensionFromUrl(listWallpaper.path),
                            it.byteStream(),
                            it.contentLength()
                        )
                    }
            )
            .toFlowable(BackpressureStrategy.LATEST)
}