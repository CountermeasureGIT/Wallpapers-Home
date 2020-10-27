package ru.countermeasure.wallpapershome.domain.model


import com.google.gson.annotations.SerializedName

data class WallpaperInfoResponse(
    @SerializedName("data")
    val data: DetailedWallpaper?
)