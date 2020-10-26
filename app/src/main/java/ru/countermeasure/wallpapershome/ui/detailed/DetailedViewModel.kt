package ru.countermeasure.wallpapershome.ui.detailed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.countermeasure.wallpapershome.base.BaseViewModel
import ru.countermeasure.wallpapershome.model.ListWallpaper
import ru.countermeasure.wallpapershome.network.WallheavenApi
import ru.countermeasure.wallpapershome.network.WallheavenService

class DetailedViewModel(
    private val listWallpaper: ListWallpaper,
    private val wallheavenApi: WallheavenService = WallheavenApi
) : BaseViewModel() {

    init {
        fetchWallpaperDetailsInfo(listWallpaper.id)
    }

    private fun fetchWallpaperDetailsInfo(wallpaperId: String) {
        //wallheavenApi.getWallpaperInfo(wallpaperId)
    }

    @Suppress("UNCHECKED_CAST")
    class DetailedViewModelFactory(
        private val listWallpaper: ListWallpaper
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            DetailedViewModel(listWallpaper) as T
    }
}