package ru.countermeasure.wallpapershome.ui.latest

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_latest.*
import ru.countermeasure.wallpapershome.R
import ru.countermeasure.wallpapershome.WallpaperAdapter

class LatestListFragment : Fragment(R.layout.fragment_latest) {
    private val viewModel: LatestViewModel by viewModels()
    private val renderDisposables = CompositeDisposable()
    private val halfScreen by lazy {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

        val listPadding = resources.getDimension(R.dimen.wallpaper_list_padding).toInt() * 2
        (displayMetrics.widthPixels - listPadding) / 2
    }
    private val wallpaperAdapter by lazy {
        WallpaperAdapter(
            halfScreen
        ).apply {
            addLoadStateListener { loadStates ->
                when (loadStates.append) {
                    is LoadState.Loading -> {
                        Log.d("TAG", "loading")
                        swipeToRefreshBottom.post {
                            swipeToRefreshBottom.isRefreshing = true
                        }
                    }
                    is LoadState.NotLoading -> {
                        Log.d("TAG", "not loading")
                        swipeToRefreshBottom.post {
                            swipeToRefreshBottom.isRefreshing = false
                        }
                    }
                    is LoadState.Error -> {
                        Log.d("TAG", "error")
                        swipeToRefreshBottom.post {
                            swipeToRefreshBottom.isRefreshing = false
                        }
                    }
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        wallpapersRecyclerView.apply {
            adapter = wallpaperAdapter
            layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                    gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
                }
            setHasFixedSize(true)
        }
        swipeToRefreshBottom.isEnabled = false
    }

    override fun onResume() {
        super.onResume()
        with(viewModel) {
            renderDisposables.addAll(
                data.subscribe {
                    wallpaperAdapter.submitData(lifecycle, it)
                },
                loading.subscribe {
                    swipeToRefresh.post { swipeToRefresh.isRefreshing = it }
                },
                error.subscribe { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
            )
        }
    }

    override fun onPause() {
        super.onPause()
        renderDisposables.clear()
    }
}