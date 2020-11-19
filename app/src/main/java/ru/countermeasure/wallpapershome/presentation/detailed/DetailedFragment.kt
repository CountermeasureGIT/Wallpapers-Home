package ru.countermeasure.wallpapershome.presentation.detailed

import android.app.WallpaperManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detailed.*
import ru.countermeasure.wallpapershome.R
import ru.countermeasure.wallpapershome.domain.models.ListWallpaper
import ru.countermeasure.wallpapershome.presentation._system.base.BaseFragment
import ru.countermeasure.wallpapershome.utils.runOnUiThread
import ru.countermeasure.wallpapershome.utils.screenWidth
import kotlin.math.round


@AndroidEntryPoint
class DetailedFragment : BaseFragment() {
    override val layoutRes: Int = R.layout.fragment_detailed
    private val listWallpaper: ListWallpaper by lazy {
        requireNotNull(requireArguments().getParcelable(ARG_LIST_WALLPAPER)) {
            "Must have ListWallpaper argument"
        }
    }

    private val calculatedImageWidth by lazy { screenWidth() }
    private val calculatedImageHeight by lazy {
        val ratio = listWallpaper.trueRatio
        round(calculatedImageWidth / ratio).toInt()
    }

    private val viewModel: DetailedViewModel by viewModels()
    private val wallpaperManager by lazy {
        WallpaperManager.getInstance(requireContext().applicationContext)
    }

    private val previewImageListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            Log.d("TEST", "failed to load preview")
            startPostponedEnterTransition()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            Log.d("TEST", "loaded preview")
            startPostponedEnterTransition()
            return false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }

        postponeEnterTransition()

        wallpaperViewSwitcher.updateLayoutParams {
            width = calculatedImageWidth
            height = calculatedImageHeight
        }

        Glide.with(this)
            .load(listWallpaper.thumbs?.original)
            .addListener(previewImageListener)
            .into(wallpaperPreviewImageView)

        setOnHomeScreenButton.setOnClickListener {
            wallpaperViewSwitcher.showNext()
        }
    }

    override fun onResume() {
        super.onResume()
        with(viewModel) {
            disposeOnPause(imageDownloadStateObservable.subscribe { state ->
                Log.d(this@DetailedFragment.javaClass.simpleName, "State: $state")
                when (state) {
                    is ImageDownloadUiState.Success -> {
                        imageLoadProgressBar.isVisible = false

                        Glide.with(this@DetailedFragment)
                            .load(state.file)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .addListener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    runOnUiThread {
                                        if (isAdded && isResumed) {
                                            resource?.let {
                                                wallpaperViewSwitcher?.setImageDrawable(
                                                    it
                                                )
                                            }
                                            Glide.with(this@DetailedFragment)
                                                .clear(wallpaperPreviewImageView)
                                        }
                                    }
                                    return false
                                }
                            })
                            .submit(calculatedImageWidth, calculatedImageHeight)
                    }
                    is ImageDownloadUiState.Downloading -> {
                        imageLoadProgressBar.progress = state.progress
                        if (!imageLoadProgressBar.isVisible)
                            imageLoadProgressBar.isVisible = true
                    }
                    is ImageDownloadUiState.Failure -> {
                        imageLoadProgressBar.isVisible = false
                        val errorMessage = state.error.getMessageText(resources)
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
            )

        }
    }

    companion object {
        private const val ARG_LIST_WALLPAPER = DetailedViewModel.ARG_LIST_WALLPAPER

        fun createBundle(listWallpaper: ListWallpaper) =
            bundleOf(
                ARG_LIST_WALLPAPER to listWallpaper
            )
    }
}