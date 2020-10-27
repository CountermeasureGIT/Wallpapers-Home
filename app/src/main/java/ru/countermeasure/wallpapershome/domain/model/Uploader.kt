package ru.countermeasure.wallpapershome.domain.model


import com.google.gson.annotations.SerializedName

data class Uploader(
    @SerializedName("username")
    val username: String?,
    @SerializedName("group")
    val group: String?,
    @SerializedName("avatar")
    val avatar: Avatar?
)