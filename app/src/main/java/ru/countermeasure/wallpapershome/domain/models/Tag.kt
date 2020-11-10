package ru.countermeasure.wallpapershome.domain.models


import com.google.gson.annotations.SerializedName

data class Tag(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("alias")
    val alias: String?,
    @SerializedName("category_id")
    val categoryId: Int?,
    @SerializedName("category")
    val category: String?,
    @SerializedName("purity")
    val purity: String?,
    @SerializedName("created_at")
    val createdAt: String?
)