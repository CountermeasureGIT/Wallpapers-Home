package ru.countermeasure.wallpapershome.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.reactivex.Observable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_wallpaper_card.*
import ru.countermeasure.wallpapershome.R
import ru.countermeasure.wallpapershome.domain.models.ListWallpaper
import kotlin.math.round

class WallpaperAdapter(
    private val halfScreen: Int,
    private val onItemClick: (wallpaper: ListWallpaper) -> Unit
) :
    PagingDataAdapter<ListWallpaper, WallpaperAdapter.WallpaperViewHolder>(
        WallpaperDiffUtilCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperViewHolder {
        return WallpaperViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_wallpaper_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class WallpaperViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        private var item: ListWallpaper? = null

        init {
            wallpaperImageView.setOnClickListener {
                item?.let { onItemClick(it) }
            }
        }

        fun bind(wallpaper: ListWallpaper?) {
            item = wallpaper

            wallpaper?.let {
                wallpaperImageView.updateLayoutParams {
                    width = halfScreen
                    height = round(halfScreen / wallpaper.trueRatio).toInt()
                }

                Glide.with(wallpaperImageView.context)
                    .load(wallpaper.thumbs?.original)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(wallpaperImageView)
            }
        }

    }
}

class WallpaperDiffUtilCallback : DiffUtil.ItemCallback<ListWallpaper>() {
    override fun areItemsTheSame(oldItem: ListWallpaper, newItem: ListWallpaper): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ListWallpaper, newItem: ListWallpaper): Boolean {
        return oldItem == newItem
    }
}