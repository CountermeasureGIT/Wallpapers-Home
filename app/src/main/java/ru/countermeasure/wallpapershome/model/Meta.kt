package ru.countermeasure.wallpapershome.model


import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("current_page")
    val currentPage: Int?,
    @SerializedName("last_page")
    val lastPage: Int?,
    @SerializedName("per_page")
    val perPage: Int?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("query")
    val query: Any?,
    @SerializedName("seed")
    val seed: Any?
)