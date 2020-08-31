package ru.countermeasure.wallpapershome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_wallpaper_card.*
import ru.countermeasure.wallpapershome.model.Wallpaper

class WallpaperAdapter(private val halfScreen: Int) :
    PagingDataAdapter<Wallpaper, WallpaperAdapter.WallpaperViewHolder>(
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

        fun bind(wallpaper: Wallpaper?) {
            if (wallpaper != null) {
                wallpaperImageView.layoutParams.width = halfScreen
                wallpaperImageView.layoutParams.height = (halfScreen / wallpaper.ratio).toInt()

                Glide.with(wallpaperImageView.context)
                    .load(wallpaper.thumbs?.original)
                    .into(wallpaperImageView)
            } else {
                Glide.with(wallpaperImageView.context)
                    .load(R.drawable.ic_launcher_background)
                    .into(wallpaperImageView)
            }
        }

    }
}

class WallpaperDiffUtilCallback : DiffUtil.ItemCallback<Wallpaper>() {
    override fun areItemsTheSame(oldItem: Wallpaper, newItem: Wallpaper): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Wallpaper, newItem: Wallpaper): Boolean {
        return oldItem == newItem
    }
}