package ru.countermeasure.wallpapershome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_wallpaper_card.*
import ru.countermeasure.wallpapershome.model.ListWallpaper
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
            number.text = (bindingAdapterPosition / 24 + 1).toString()
            imageLoadingProgressBar.isVisible = wallpaper == null
            wallpaperImageView.isVisible = wallpaper != null

            wallpaper?.let {
                wallpaperImageView.layoutParams.width = halfScreen
                wallpaperImageView.layoutParams.height =
                    round(halfScreen / (wallpaper.dimensionX.toDouble() / wallpaper.dimensionY)).toInt()

                Glide.with(wallpaperImageView.context)
                    .load(wallpaper.thumbs?.original)
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