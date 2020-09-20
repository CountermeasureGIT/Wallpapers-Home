package ru.countermeasure.wallpapershome.ui.random

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.rxjava2.cachedIn
import androidx.paging.rxjava2.flowable
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.countermeasure.wallpapershome.WallpapersPagingSource
import ru.countermeasure.wallpapershome.model.Wallpaper
import ru.countermeasure.wallpapershome.network.Filter
import ru.countermeasure.wallpapershome.network.WallheavenApi
import ru.countermeasure.wallpapershome.network.WallheavenService

class RandomListViewModel(
    private val wallheavenService: WallheavenService = WallheavenApi
) : ViewModel() {
    private var currentFilter = Filter(
        sorting = Filter.Sorting.RANDOM,
        topRange = Filter.TopRange.M1,
        categories = listOf(Filter.Category.ANIME, Filter.Category.GENERAL, Filter.Category.PEOPLE)
    )

    private val loadingRelay = BehaviorRelay.createDefault(false)
    private val dataRelay = BehaviorRelay.create<PagingData<Wallpaper>>()
    private val errorRelay = BehaviorRelay.create<String>()
    private val filterRelay = BehaviorRelay.createDefault(currentFilter)

    val data: Observable<PagingData<Wallpaper>> = dataRelay.hide()
    val loading: Observable<Boolean> = loadingRelay.hide()
    val error: Observable<String> = errorRelay.hide()
    val filter: Observable<Filter> = filterRelay.hide()

    private lateinit var pagingSource: PagingSource<Int, Wallpaper>
    private val pager: Pager<Int, Wallpaper> =
        Pager(PagingConfig(pageSize = 24, prefetchDistance = 8, initialLoadSize = 1)) {
            WallpapersPagingSource(wallheavenService, filterRelay.value!!).also {
                pagingSource = it
            }
        }

    private val compositeDisposable = CompositeDisposable()

    init {
        fetchWallpapers()
    }

    private fun fetchWallpapers() {
        compositeDisposable.add(pager.flowable.cachedIn(viewModelScope)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingRelay.accept(true) }
            .doAfterNext { loadingRelay.accept(false) }
            .subscribe {
                dataRelay.accept(it)
            }
        )
    }

    fun onFilterChange(newFilter: Filter) {
        filterRelay.accept(newFilter)
        pagingSource.invalidate()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun onRefresh() {
        pagingSource.invalidate()
    }
}