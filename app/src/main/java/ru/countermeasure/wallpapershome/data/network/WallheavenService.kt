package ru.countermeasure.wallpapershome.data.network

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import ru.countermeasure.wallpapershome.domain.models.WallpaperInfoResponse
import ru.countermeasure.wallpapershome.domain.models.WallpapersListResponse

interface WallheavenService {

    @GET("search")
    fun getList(
        @QueryMap queryMap: Map<String, String>
    ): Single<WallpapersListResponse>

    @GET("/w/{id}")
    fun getWallpaperInfo(@Path("id") id: String): Single<WallpaperInfoResponse>

    @Streaming
    @GET
    fun downloadImage(@Url imageUrl: String): Call<ResponseBody>
}