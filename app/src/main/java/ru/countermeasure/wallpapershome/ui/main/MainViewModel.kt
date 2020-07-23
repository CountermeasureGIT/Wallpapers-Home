package ru.countermeasure.wallpapershome.ui.main

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay3.BehaviorRelay
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.countermeasure.wallpapershome.model.Wallpaper
import ru.countermeasure.wallpapershome.network.WallheavenApi

class MainViewModel : ViewModel() {

    private val wallheavenService = WallheavenApi

    private val loadingRelay = BehaviorRelay.createDefault(false)
    private val dataRelay = BehaviorRelay.createDefault<List<Wallpaper>>(emptyList())
    private val errorRelay = BehaviorRelay.create<String>()

    val data: Observable<List<Wallpaper>> = dataRelay.hide()
    val loading: Observable<Boolean> = loadingRelay.hide()
    val error: Observable<String> = errorRelay.hide()

    init {
        fetchWallpapers()
    }

    private fun fetchWallpapers() {
        wallheavenService.getList()
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
            )
    }

}