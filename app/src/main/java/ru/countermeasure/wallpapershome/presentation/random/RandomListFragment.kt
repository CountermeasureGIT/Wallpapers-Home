package ru.countermeasure.wallpapershome.presentation.random

import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_random.*
import ru.countermeasure.wallpapershome.R
import ru.countermeasure.wallpapershome.base.BaseFragment
import ru.countermeasure.wallpapershome.presentation.WallpaperAdapter
import ru.countermeasure.wallpapershome.presentation.detailed.DetailedFragment
import ru.countermeasure.wallpapershome.utils.navigateTo
import ru.countermeasure.wallpapershome.utils.setSlideAnimation

@AndroidEntryPoint
class RandomListFragment : BaseFragment() {
    override val layoutRes: Int = R.layout.fragment_random

    private val viewModel: RandomListViewModel by viewModels()
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
            adapter = wallpaperAdapter
            layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                    gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
                }
            setHasFixedSize(true)
        }
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
                },
                error.subscribe { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
            )
        }
    }
}