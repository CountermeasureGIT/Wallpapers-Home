package ru.countermeasure.wallpapershome

import androidx.paging.rxjava2.RxPagingSource
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.countermeasure.wallpapershome.model.Filter
import ru.countermeasure.wallpapershome.model.ListWallpaper
import ru.countermeasure.wallpapershome.model.WallpapersListResponse
import ru.countermeasure.wallpapershome.network.WallheavenService

class WallpapersPagingSource(
    val api: WallheavenService,
    val filter: Filter
) : RxPagingSource<Int, ListWallpaper>() {

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ListWallpaper>> {
        val pageToLoad = (params.key ?: STARTING_PAGE_INDEX)

        return Single.just(filter.getQuery(pageToLoad))
            .subscribeOn(Schedulers.computation())
            .flatMap { api.getList(it) }
            .subscribeOn(Schedulers.io())
            .map { toLoadResult(pageToLoad, it) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(
        currentPage: Int,
        response: WallpapersListResponse
    ): LoadResult<Int, ListWallpaper> {
        return LoadResult.Page(
            response.data,
            prevKey = if (currentPage == STARTING_PAGE_INDEX) null else currentPage - 1,
            nextKey = if (response.data.isEmpty()) null else currentPage + 1
        )
    }
}