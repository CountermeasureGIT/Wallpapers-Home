package ru.countermeasure.wallpapershome.domain.model


import com.google.gson.annotations.SerializedName

data class WallpapersListResponse(
    @SerializedName("data")
    val data: List<ListWallpaper>,
    @SerializedName("meta")
    val meta: Meta
)