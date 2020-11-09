package ru.countermeasure.wallpapershome.presentation.detailed

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import ru.countermeasure.wallpapershome.data.network.WallheavenService
import ru.countermeasure.wallpapershome.domain.model.ListWallpaper
import ru.countermeasure.wallpapershome.presentation._system.base.BaseViewModel

class DetailedViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val wallheavenApi: WallheavenService
) : BaseViewModel() {

    companion object {
        const val ARG_LIST_WALLPAPER = "ARG_LIST_WALLPAPER"
    }

    private val listWallpaper: ListWallpaper =
        requireNotNull(savedStateHandle.get<ListWallpaper>(ARG_LIST_WALLPAPER)) {
            "Must have ListWallpaper argument"
        }

    init {
        fetchWallpaperDetailsInfo(listWallpaper.id)
    }

    fun test() {
        fetchWallpaperDetailsInfo(listWallpaper.id)
    }

    private fun fetchWallpaperDetailsInfo(wallpaperId: String) {
        Log.d("TEST", wallpaperId)
        //wallheavenApi.getWallpaperInfo(wallpaperId)
    }
}