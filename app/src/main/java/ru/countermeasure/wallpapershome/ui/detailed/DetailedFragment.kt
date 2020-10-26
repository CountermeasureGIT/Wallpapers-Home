package ru.countermeasure.wallpapershome.ui.detailed

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_detailed.*
import ru.countermeasure.wallpapershome.R
import ru.countermeasure.wallpapershome.model.ListWallpaper

class DetailedFragment : Fragment(R.layout.fragment_detailed) {

    private val listWallpaper: ListWallpaper by lazy {
        requireArguments().getParcelable(ARG_LIST_WALLPAPER)!!
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

    companion object {
        private const val ARG_LIST_WALLPAPER = "ARG_LIST_WALLPAPER"

        fun createBundle(listWallpaper: ListWallpaper) =
            bundleOf(
                ARG_LIST_WALLPAPER to listWallpaper
            )
    }
}