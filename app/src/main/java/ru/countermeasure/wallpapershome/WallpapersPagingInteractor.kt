package ru.countermeasure.wallpapershome

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.rxjava2.flowable
import io.reactivex.Flowable
import ru.countermeasure.wallpapershome.model.Wallpaper
import ru.countermeasure.wallpapershome.network.Filter
import ru.countermeasure.wallpapershome.network.WallheavenService

class WallpapersPagingInteractor(
    private val wallheavenService: WallheavenService
) {
    companion object {
        private const val PAGE_SIZE = 24
        private const val PREFETCH_DISTANCE = 8
        private const val INITIAL_LOAD_SIZE = 1
    }

    private var pager: Pager<Int, Wallpaper>? = null
    private lateinit var filter: Filter
    private lateinit var pagingSource: PagingSource<Int, Wallpaper>
    private lateinit var pagerFlowable: Flowable<PagingData<Wallpaper>>

    fun getPagedListForFilter(newFilter: Filter): Flowable<PagingData<Wallpaper>> {
        filter = newFilter
        if (pager == null) {
            pager = createPager().also {
                pagerFlowable = it.flowable
            }
        } else {
            pagingSource.invalidate()
        }
        return pagerFlowable
    }

    private fun createPager(): Pager<Int, Wallpaper> {
        return Pager(
            PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                initialLoadSize = INITIAL_LOAD_SIZE
            )
        ) {
            WallpapersPagingSource(wallheavenService, filter).also {
                pagingSource = it
            }
        }
    }


}