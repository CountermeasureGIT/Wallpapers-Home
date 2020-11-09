package ru.countermeasure.wallpapershome.interactor

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.rxjava2.flowable
import io.reactivex.Flowable
import ru.countermeasure.wallpapershome.WallpapersPagingSource
import ru.countermeasure.wallpapershome.data.network.WallheavenService
import ru.countermeasure.wallpapershome.domain.model.Filter
import ru.countermeasure.wallpapershome.domain.model.ListWallpaper
import javax.inject.Inject

class WallpapersPagingInteractor @Inject constructor(
    private val wallheavenService: WallheavenService
) {
    companion object {
        private const val PAGE_SIZE = 2
        private const val PREFETCH_DISTANCE = PAGE_SIZE
        private const val INITIAL_LOAD_SIZE = PAGE_SIZE * 2
    }

    private lateinit var pager: Pager<Int, ListWallpaper>
    private lateinit var filter: Filter
    private lateinit var pagingSource: PagingSource<Int, ListWallpaper>

    fun getWallpapersListStream(newFilter: Filter): Flowable<PagingData<ListWallpaper>> {
        filter = newFilter
        pager = createPager()
        return pager.flowable
    }

    fun changeFilter(newFilter: Filter) {
        if (filter == newFilter)
            return
        filter = newFilter
        pagingSource.invalidate()
    }

    private fun createPager(): Pager<Int, ListWallpaper> {
        return Pager(
            PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = INITIAL_LOAD_SIZE,
                prefetchDistance = PREFETCH_DISTANCE
            )
        ) {
            WallpapersPagingSource(wallheavenService, filter).also {
                pagingSource = it
            }
        }
    }


}