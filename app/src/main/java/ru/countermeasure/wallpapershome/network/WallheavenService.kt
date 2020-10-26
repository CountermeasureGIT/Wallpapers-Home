package ru.countermeasure.wallpapershome.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.countermeasure.wallpapershome.model.WallpaperInfoResponse
import ru.countermeasure.wallpapershome.model.WallpapersListResponse

interface WallheavenService {

    @GET("search")
    fun getList(
        @QueryMap queryMap: Map<String, String>
    ): Single<WallpapersListResponse>

    @GET("/w/{id}")
    fun getWallpaperInfo(@Path("id") id: String): Single<WallpaperInfoResponse>
}