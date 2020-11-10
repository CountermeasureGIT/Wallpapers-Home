package ru.countermeasure.wallpapershome.domain.interactors

import io.reactivex.Single
import ru.countermeasure.wallpapershome.domain.abstractions.WallpapersRepository
import ru.countermeasure.wallpapershome.domain.models.ListWallpaper
import java.io.File
import javax.inject.Inject

class WallpaperDetailedInteractor @Inject constructor(
    private val wallpapersRepository: WallpapersRepository
) {
    fun getImage(listWallpaper: ListWallpaper): Single<File> =
        wallpapersRepository.getImageFile(listWallpaper.id, listWallpaper.url)
}