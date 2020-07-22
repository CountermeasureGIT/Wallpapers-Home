package ru.countermeasure.wallpapershome.network

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import ru.countermeasure.wallpapershome.model.Wallpapers

interface WallheavenApi {

    @GET("search")
    fun getList(): Single<Wallpapers>

}