package ru.countermeasure.wallpapershome

import androidx.paging.rxjava2.RxPagingSource
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.countermeasure.wallpapershome.model.Wallpaper
import ru.countermeasure.wallpapershome.model.WallpapersDataHolder
import ru.countermeasure.wallpapershome.network.WallheavenService

class WallpapersPagingSource(
    val api: WallheavenService
) : RxPagingSource<Int, Wallpaper>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Wallpaper>> {
        val nextPageNumber = params.key ?: 1

        return api.getTopList(nextPageNumber)
            .subscribeOn(Schedulers.io())
            .map(this::toLoadResult)
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(response: WallpapersDataHolder): LoadResult<Int, Wallpaper> {
        return LoadResult.Page(
            response.data,
            null,
            response.meta?.currentPage?.plus(1),
            LoadResult.Page.COUNT_UNDEFINED,
            LoadResult.Page.COUNT_UNDEFINED
        )
    }
}