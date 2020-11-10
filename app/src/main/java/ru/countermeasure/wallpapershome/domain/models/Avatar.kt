package ru.countermeasure.wallpapershome.domain.models


import com.google.gson.annotations.SerializedName

data class Avatar(
    @SerializedName("200px")
    val px200: String?,
    @SerializedName("128px")
    val px128: String?,
    @SerializedName("32px")
    val px32: String?,
    @SerializedName("20px")
    val px20: String?
)