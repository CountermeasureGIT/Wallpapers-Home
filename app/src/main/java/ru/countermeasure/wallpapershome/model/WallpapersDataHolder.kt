package ru.countermeasure.wallpapershome.model


import com.google.gson.annotations.SerializedName

data class WallpapersDataHolder(
    @SerializedName("data")
    val data: List<Wallpaper>,
    @SerializedName("meta")
    val meta: Meta
)