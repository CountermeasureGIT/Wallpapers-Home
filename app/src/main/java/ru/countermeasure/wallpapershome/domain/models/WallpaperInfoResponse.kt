package ru.countermeasure.wallpapershome.domain.models


import com.google.gson.annotations.SerializedName

data class WallpaperInfoResponse(
    @SerializedName("data")
    val data: DetailedWallpaper?
)