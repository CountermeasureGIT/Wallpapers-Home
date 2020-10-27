package ru.countermeasure.wallpapershome.presentation.detailed

import android.app.WallpaperManager
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detailed.*
import ru.countermeasure.wallpapershome.R
import ru.countermeasure.wallpapershome.domain.model.ListWallpaper

@AndroidEntryPoint
class DetailedFragment : Fragment(R.layout.fragment_detailed) {

    private val listWallpaper: ListWallpaper by lazy {
        requireNotNull(requireArguments().getParcelable(ARG_LIST_WALLPAPER)) {
            "Must have ListWallpaper argument"
        }
    }

    private val detailedViewModel: DetailedViewModel by viewModels()

    private val wallpaperManager by lazy {
        WallpaperManager.getInstance(requireContext().applicationContext)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }

        Glide.with(this)
            .load(listWallpaper.path)
            .thumbnail(
                Glide.with(requireContext()).load(listWallpaper.thumbs?.original)
            )
            .into(wallpaperImageView)
    }

    override fun onResume() {
        super.onResume()
        detailedViewModel.test()
    }

    companion object {
        private const val ARG_LIST_WALLPAPER = DetailedViewModel.ARG_LIST_WALLPAPER

        fun createBundle(listWallpaper: ListWallpaper) =
            bundleOf(
                ARG_LIST_WALLPAPER to listWallpaper
            )
    }
}