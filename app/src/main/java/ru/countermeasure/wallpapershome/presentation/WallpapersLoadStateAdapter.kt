package ru.countermeasure.wallpapershome.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_load_state_footer_wallpapers.*
import ru.countermeasure.wallpapershome.R

class WallpapersLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<WallpapersLoadStateAdapter.WallpapersLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: WallpapersLoadStateViewHolder, loadState: LoadState) =
        holder.bind(loadState)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): WallpapersLoadStateViewHolder =
        WallpapersLoadStateViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_load_state_footer_wallpapers, parent, false)
        )

    inner class WallpapersLoadStateViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            retryButton.setOnClickListener { retry() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                errorTextView.text = loadState.error.localizedMessage
            }

            val isLoading = loadState is LoadState.Loading

            progressBar.isVisible = isLoading
            retryButton.isVisible = !isLoading
            errorTextView.isVisible = !isLoading
        }
    }

}