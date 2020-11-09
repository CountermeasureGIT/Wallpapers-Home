package ru.countermeasure.wallpapershome.presentation.toplist

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_toplist.*
import ru.countermeasure.wallpapershome.R
import ru.countermeasure.wallpapershome.presentation.WallpaperAdapter
import ru.countermeasure.wallpapershome.presentation.WallpapersLoadStateAdapter
import ru.countermeasure.wallpapershome.presentation._system.base.BaseFragment
import ru.countermeasure.wallpapershome.presentation.detailed.DetailedFragment
import ru.countermeasure.wallpapershome.utils.navigateTo
import ru.countermeasure.wallpapershome.utils.screenWidth
import ru.countermeasure.wallpapershome.utils.setSlideAnimation

@AndroidEntryPoint
class TopListFragment : BaseFragment() {
    override val layoutRes: Int = R.layout.fragment_toplist
    private val viewModel: TopListViewModel by viewModels()

    private val halfScreen by lazy { screenWidth() / 2 }
    private val wallpaperAdapter by lazy {
        WallpaperAdapter(halfScreen) {
            activity?.supportFragmentManager?.navigateTo(
                DetailedFragment::class.java,
                args = DetailedFragment.createBundle(it),
                setupFragmentTransaction = {
                    it.setSlideAnimation()
                }
            )
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun onResume() {
        super.onResume()
        with(viewModel) {
            disposeOnPause(
                data.subscribe {
                    wallpaperAdapter.submitData(lifecycle, it)
                },
                loading.subscribe {
                    swipeToRefresh.post { swipeToRefresh.isRefreshing = it }
                }
            )
        }
    }
}