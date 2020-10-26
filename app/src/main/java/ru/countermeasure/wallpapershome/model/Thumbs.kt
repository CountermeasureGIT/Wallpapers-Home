package ru.countermeasure.wallpapershome.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Thumbs(
    @SerializedName("large")
    val large: String?,
    @SerializedName("original")
    val original: String?,
    @SerializedName("small")
    val small: String?
) : Parcelable