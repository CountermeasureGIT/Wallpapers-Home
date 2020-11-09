package ru.countermeasure.wallpapershome.presentation.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.countermeasure.wallpapershome.data.network.WallheavenService
import ru.countermeasure.wallpapershome.domain.model.Filter
import ru.countermeasure.wallpapershome.domain.model.ListWallpaper
import ru.countermeasure.wallpapershome.interactor.WallpapersPagingInteractor
import ru.countermeasure.wallpapershome.presentation._system.base.BaseViewModel
import ru.countermeasure.wallpapershome.presentation.search.search_filter.SearchFilterPublisher

class SearchViewModel @ViewModelInject constructor(
    private val searchFilterPublisher: SearchFilterPublisher,
    private val wallheavenService: WallheavenService,
    private val wallpapersInteractor: WallpapersPagingInteractor
) : BaseViewModel() {

    private val defaultFilter = Filter(
        sorting = Filter.Sorting.RELEVANCE,
        order = Filter.Order.DESC,
        categories = listOf(Filter.Category.ANIME, Filter.Category.GENERAL, Filter.Category.PEOPLE)
    )

    private val loadingRelay = BehaviorRelay.createDefault(false)
    private val dataRelay = BehaviorRelay.create<PagingData<ListWallpaper>>()
    private val errorRelay = BehaviorRelay.create<String>()

    val data: Observable<PagingData<ListWallpaper>> = dataRelay.hide()
    val loading: Observable<Boolean> = loadingRelay.hide()
    val error: Observable<String> = errorRelay.hide()

    init {
        searchFilterPublisher.apply {
            sharedDefaultFilter = this@SearchViewModel.defaultFilter
            acceptFilter(sharedDefaultFilter)

            filterObservable.subscribe {
                fetchData(it)
            }.collect()
        }
    }

    private fun fetchData(filter: Filter) {
        wallpapersInteractor.getWallpapersListStream(filter)
            .cachedIn(viewModelScope)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingRelay.accept(true) }
            .doAfterNext { loadingRelay.accept(false) }
            .subscribe {
                dataRelay.accept(it)
            }.collect()
    }
}