package ru.countermeasure.wallpapershome.presentation.detailed

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.countermeasure.wallpapershome.R
import ru.countermeasure.wallpapershome.domain.interactors.WallpaperDetailedInteractor
import ru.countermeasure.wallpapershome.domain.models.ListWallpaper
import ru.countermeasure.wallpapershome.domain.models.OperationStatus
import ru.countermeasure.wallpapershome.presentation._system.base.BaseViewModel
import ru.countermeasure.wallpapershome.utils.UiError
import java.io.File

sealed class ImageDownloadUiState {
    data class Downloading(
        val progress: Int
    ) : ImageDownloadUiState()

    data class Success(
        val file: File
    ) : ImageDownloadUiState()

    data class Failure(
        val error: UiError
    ) : ImageDownloadUiState()
}

class DetailedViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val wallpaperDetailedInteractor: WallpaperDetailedInteractor
) : BaseViewModel() {

    companion object {
        const val ARG_LIST_WALLPAPER = "ARG_LIST_WALLPAPER"
    }

    private val listWallpaper: ListWallpaper =
        requireNotNull(savedStateHandle.get<ListWallpaper>(ARG_LIST_WALLPAPER)) {
            "Must have ListWallpaper argument"
        }

    private val imageDownloadStateRelay = BehaviorRelay.create<ImageDownloadUiState>()

    val imageDownloadStateObservable: Observable<ImageDownloadUiState> =
        imageDownloadStateRelay.hide().observeOn(AndroidSchedulers.mainThread())

    init {
        fetchWallpaperImage()
    }

    private fun fetchWallpaperImage() {
        wallpaperDetailedInteractor.getImage(listWallpaper)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d(this.javaClass.simpleName, "Status: $it")
                    when (it) {
                        is OperationStatus.InProgress -> {
                            imageDownloadStateRelay.accept(ImageDownloadUiState.Downloading(it.progress))
                        }
                        is OperationStatus.Finished<*> -> {
                            val resultFile = it.result as File
                            imageDownloadStateRelay.accept(ImageDownloadUiState.Success(resultFile))
                        }
                    }
                },
                { exception ->
                    val error = exception.message?.let { UiError(it) } ?: UiError(
                        null,
                        R.string.detailed_screen_get_image_error
                    )
                    imageDownloadStateRelay.accept(ImageDownloadUiState.Failure(error))

                    Log.e(this.javaClass.simpleName, "Get image error: ", exception)
                },
                {
                    Log.d(this.javaClass.simpleName, "Complete")
                }
            ).collect()
    }
}