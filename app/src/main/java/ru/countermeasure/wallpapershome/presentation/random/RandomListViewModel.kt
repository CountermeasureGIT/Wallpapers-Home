package ru.countermeasure.wallpapershome.presentation.random

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.countermeasure.wallpapershome.base.BaseViewModel
import ru.countermeasure.wallpapershome.data.network.WallheavenService
import ru.countermeasure.wallpapershome.domain.model.Filter
import ru.countermeasure.wallpapershome.domain.model.ListWallpaper
import ru.countermeasure.wallpapershome.interactor.WallpapersPagingInteractor

class RandomListViewModel @ViewModelInject constructor(
    private val wallheavenService: WallheavenService,
    private val wallpapersInteractor: WallpapersPagingInteractor
) : BaseViewModel() {
    private var currentFilter = Filter(
        sorting = Filter.Sorting.RANDOM,
        topRange = Filter.TopRange.M1,
        categories = listOf(/*Filter.Category.ANIME, */Filter.Category.GENERAL /*Filter.Category.PEOPLE*/)
    )

    private val loadingRelay = BehaviorRelay.createDefault(false)
    private val dataRelay = BehaviorRelay.create<PagingData<ListWallpaper>>()
    private val errorRelay = BehaviorRelay.create<String>()
    private val filterRelay = BehaviorRelay.createDefault(currentFilter)

    val data: Observable<PagingData<ListWallpaper>> = dataRelay.hide()
    val loading: Observable<Boolean> = loadingRelay.hide()
    val error: Observable<String> = errorRelay.hide()
    val filter: Observable<Filter> = filterRelay.hide()

    init {
        wallpapersInteractor.getWallpapersListStream(currentFilter)
            .cachedIn(viewModelScope)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingRelay.accept(true) }
            .doAfterNext { loadingRelay.accept(false) }
            .subscribe {
                dataRelay.accept(it)
            }.collect()
    }

    fun onFilterChange(newFilter: Filter) {
        filterRelay.accept(newFilter)
    }
}