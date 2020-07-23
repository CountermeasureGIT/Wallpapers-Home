package ru.countermeasure.wallpapershome.model


import com.google.gson.annotations.SerializedName

data class Wallpapers(
    @SerializedName("data")
    val data: List<Data>?,
    @SerializedName("meta")
    val meta: Meta?
)