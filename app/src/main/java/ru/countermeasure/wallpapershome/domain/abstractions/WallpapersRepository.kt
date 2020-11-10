package ru.countermeasure.wallpapershome.domain.abstractions

import io.reactivex.Single
import java.io.File

interface WallpapersRepository {
    fun getImageFile(id: String, url: String): Single<File>
}