package ru.countermeasure.wallpapershome.presentation.toplist

import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_toplist.*
import ru.countermeasure.wallpapershome.R
import ru.countermeasure.wallpapershome.presentation.WallpaperAdapter
import ru.countermeasure.wallpapershome.presentation.WallpapersLoadStateAdapter
import ru.countermeasure.wallpapershome.presentation.detailed.DetailedFragment
import ru.countermeasure.wallpapershome.utils.navigateTo
import ru.countermeasure.wallpapershome.utils.setSlideAnimation

@AndroidEntryPoint
class TopListFragment : Fragment(R.layout.fragment_toplist) {

    companion object {
        fun newInstance() = TopListFragment()
    }

    private val viewModel: TopListViewModel by viewModels()
    private val renderDisposables = CompositeDisposable()
    private val halfScreen by lazy {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

        val listPadding = resources.getDimension(R.dimen.wallpaper_list_padding).toInt() * 2
        (displayMetrics.widthPixels - listPadding) / 2
    }
    private val wallpaperAdapter by lazy {
        WallpaperAdapter(halfScreen) {
            activity?.supportFragmentManager?.navigateTo(
                DetailedFragment::class.java,
                args = DetailedFragment.createBundle(it),
                setupFragmentTransaction = {
                    it.setSlideAnimation()
                })
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        wallpapersRecyclerView.apply {
            adapter = wallpaperAdapter.withLoadStateFooter(
                footer = WallpapersLoadStateAdapter {
                    wallpaperAdapter.retry()
                }
            )
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