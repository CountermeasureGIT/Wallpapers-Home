package ru.countermeasure.wallpapershome.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_walpaper_card.*
import ru.countermeasure.wallpapershome.R
import ru.countermeasure.wallpapershome.model.Wallpaper

class WallpaperAdapter :
    ListAdapter<Wallpaper, WallpaperAdapter.WallpaperViewHolder>(
        WallpaperDiffUtilCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperViewHolder {
        return WallpaperViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_walpaper_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WallpaperViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(wallpaper: Wallpaper) {
            Glide.with(wallpaperImageView.context)
                .load(wallpaper.thumbs?.small)
                .into(wallpaperImageView)
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