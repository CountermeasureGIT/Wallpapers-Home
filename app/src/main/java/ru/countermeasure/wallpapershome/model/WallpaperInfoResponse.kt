package ru.countermeasure.wallpapershome.model


import com.google.gson.annotations.SerializedName

data class WallpaperInfoResponse(
    @SerializedName("data")
    val data: DetailedWallpaper?
)