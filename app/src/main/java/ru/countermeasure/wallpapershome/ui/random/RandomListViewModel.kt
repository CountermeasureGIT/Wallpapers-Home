package ru.countermeasure.wallpapershome.ui.random

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.countermeasure.wallpapershome.WallpapersPagingInteractor
import ru.countermeasure.wallpapershome.model.Filter
import ru.countermeasure.wallpapershome.model.ListWallpaper
import ru.countermeasure.wallpapershome.network.WallheavenApi
import ru.countermeasure.wallpapershome.network.WallheavenService

class RandomListViewModel(
    private val wallheavenService: WallheavenService = WallheavenApi,
    private val wallpapersInteractor: WallpapersPagingInteractor = WallpapersPagingInteractor(
        wallheavenService
    )
) : ViewModel() {
    private var currentFilter = Filter(
        sorting = Filter.Sorting.RANDOM,
        topRange = Filter.TopRange.M1,
        categories = listOf(/*Filter.Category.ANIME, */Filter.Category.GENERAL, /*Filter.Category.PEOPLE*/)
    )

    private val loadingRelay = BehaviorRelay.createDefault(false)
    private val dataRelay = BehaviorRelay.create<PagingData<ListWallpaper>>()
    private val errorRelay = BehaviorRelay.create<String>()
    private val filterRelay = BehaviorRelay.createDefault(currentFilter)

    val data: Observable<PagingData<ListWallpaper>> = dataRelay.hide()
    val loading: Observable<Boolean> = loadingRelay.hide()
    val error: Observable<String> = errorRelay.hide()
    val filter: Observable<Filter> = filterRelay.hide()

    private val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.addAll(
            wallpapersInteractor.getWallpapersListStream(currentFilter)
                .cachedIn(viewModelScope)
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
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}