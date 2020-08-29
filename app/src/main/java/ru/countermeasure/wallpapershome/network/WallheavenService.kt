package ru.countermeasure.wallpapershome.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.countermeasure.wallpapershome.model.WallpapersDataHolder

interface WallheavenService {

    @GET("search")
    fun getList(): Single<WallpapersDataHolder>

    @GET("search?sorting=toplist&order=desc")
    fun getTopList(
        @Query("page") page: Int
    ): Single<WallpapersDataHolder>
}