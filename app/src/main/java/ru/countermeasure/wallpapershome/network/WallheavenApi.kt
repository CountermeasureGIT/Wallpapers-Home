package ru.countermeasure.wallpapershome.network

import com.google.gson.Gson
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.countermeasure.wallpapershome.BuildConfig
import ru.countermeasure.wallpapershome.model.WallpapersDataHolder

object WallheavenApi : WallheavenService {
    private val gson by lazy { Gson() }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addNetworkInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private val api by lazy { retrofit.create(WallheavenService::class.java) }

    override fun getList(queryMap: Map<String, String>): Single<WallpapersDataHolder> =
        api.getList(queryMap)

    override fun getList(queryMap: Map<String, String>, page: Int): Single<WallpapersDataHolder> =
        api.getList(queryMap, page)

    override fun getTopList(page: Int): Single<WallpapersDataHolder> = api.getTopList(page)
}