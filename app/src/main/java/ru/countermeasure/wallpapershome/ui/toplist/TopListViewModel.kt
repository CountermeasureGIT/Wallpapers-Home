package ru.countermeasure.wallpapershome.ui.toplist

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
import ru.countermeasure.wallpapershome.network.WallheavenApi
import ru.countermeasure.wallpapershome.network.WallheavenService

class TopListViewModel(
    private val wallheavenService: WallheavenService = WallheavenApi
) : ViewModel() {
    private val loadingRelay = BehaviorRelay.createDefault(false)
    private val dataRelay = BehaviorRelay.create<PagingData<Wallpaper>>()
    private val errorRelay = BehaviorRelay.create<String>()

    val data: Observable<PagingData<Wallpaper>> = dataRelay.hide()
    val loading: Observable<Boolean> = loadingRelay.hide()
    val error: Observable<String> = errorRelay.hide()

    private lateinit var pagingSource: PagingSource<Int, Wallpaper>
    private val pager: Pager<Int, Wallpaper> = Pager(PagingConfig(pageSize = 24)) {
        pagingSource = WallpapersPagingSource(wallheavenService)
        pagingSource
    }


    private val compositeDisposable = CompositeDisposable()

    init {
        fetchWallpapers()
    }

    private fun fetchWallpapers() {
/*        wallheavenService.getList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingRelay.accept(true) }
            .doFinally { loadingRelay.accept(false) }
            .subscribe(
                { wallpapers ->
                    wallpapers.data?.let {
                        dataRelay.accept(it)
                    }

                },
                { exception ->
                    exception.localizedMessage?.let {
                        errorRelay.accept(it)
                    }
                }
            )*/

        compositeDisposable.add(pager.flowable.cachedIn(viewModelScope)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                dataRelay.accept(it)
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun onRefresh() {
        pagingSource.invalidate()
    }
}