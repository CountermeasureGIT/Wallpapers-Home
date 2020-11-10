package ru.countermeasure.wallpapershome.presentation.toplist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.countermeasure.wallpapershome.data.network.WallheavenService
import ru.countermeasure.wallpapershome.domain.interactors.WallpapersPagingInteractor
import ru.countermeasure.wallpapershome.domain.models.Filter
import ru.countermeasure.wallpapershome.domain.models.ListWallpaper
import ru.countermeasure.wallpapershome.presentation._system.base.BaseViewModel

class TopListViewModel @ViewModelInject constructor(
    private val wallheavenService: WallheavenService,
    private val wallpapersInteractor: WallpapersPagingInteractor = WallpapersPagingInteractor(
        wallheavenService
    )
) : BaseViewModel() {
    private var currentFilter = Filter(
        sorting = Filter.Sorting.TOPLIST,
        topRange = Filter.TopRange.M1,
        order = Filter.Order.DESC,
        categories = listOf(Filter.Category.ANIME, Filter.Category.GENERAL, Filter.Category.PEOPLE)
    )

    private val loadingRelay = BehaviorRelay.createDefault(false)
    private val dataRelay = BehaviorRelay.create<PagingData<ListWallpaper>>()

    val data: Observable<PagingData<ListWallpaper>> = dataRelay.hide()
    val loading: Observable<Boolean> = loadingRelay.hide()

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
}