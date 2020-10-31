package ru.countermeasure.wallpapershome.presentation.detailed

import android.app.WallpaperManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detailed.*
import ru.countermeasure.wallpapershome.R
import ru.countermeasure.wallpapershome.base.BaseFragment
import ru.countermeasure.wallpapershome.domain.model.ListWallpaper

@AndroidEntryPoint
class DetailedFragment : BaseFragment() {
    override val layoutRes: Int = R.layout.fragment_detailed

    private val listWallpaper: ListWallpaper by lazy {
        requireNotNull(requireArguments().getParcelable(ARG_LIST_WALLPAPER)) {
            "Must have ListWallpaper argument"
        }
    }

    private val detailedViewModel: DetailedViewModel by viewModels()

    private val wallpaperManager by lazy {
        WallpaperManager.getInstance(requireContext().applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }

        val listener = object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
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
                startPostponedEnterTransition()
                return false
            }
        }

        postponeEnterTransition()
        Glide.with(this)
            .load(listWallpaper.path)
            .thumbnail(
                Glide.with(requireContext())
                    .load(listWallpaper.thumbs?.original)
                    .addListener(listener)
            )
            .addListener(listener)
            .into(wallpaperImageView)
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        private const val ARG_LIST_WALLPAPER = DetailedViewModel.ARG_LIST_WALLPAPER

        fun createBundle(listWallpaper: ListWallpaper) =
            bundleOf(
                ARG_LIST_WALLPAPER to listWallpaper
            )
    }
}