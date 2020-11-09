package ru.countermeasure.wallpapershome.presentation.search

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_toplist.wallpapersRecyclerView
import ru.countermeasure.wallpapershome.R
import ru.countermeasure.wallpapershome.presentation.WallpaperAdapter
import ru.countermeasure.wallpapershome.presentation.WallpapersLoadStateAdapter
import ru.countermeasure.wallpapershome.presentation._system.base.BaseFragment
import ru.countermeasure.wallpapershome.presentation.detailed.DetailedFragment
import ru.countermeasure.wallpapershome.presentation.search.search_filter.SearchFilterFragment
import ru.countermeasure.wallpapershome.utils.hideKeyboard
import ru.countermeasure.wallpapershome.utils.navigateTo
import ru.countermeasure.wallpapershome.utils.screenWidth
import ru.countermeasure.wallpapershome.utils.setSlideAnimation

@AndroidEntryPoint
class SearchFragment : BaseFragment() {
    override val layoutRes: Int = R.layout.fragment_search
    private val viewModel: SearchViewModel by viewModels()

    private val halfScreen by lazy { screenWidth() / 2 }
    private val wallpaperAdapter by lazy {
        WallpaperAdapter(halfScreen) {
            hideKeyboard()
            activity?.supportFragmentManager?.navigateTo(
                DetailedFragment::class.java,
                args = DetailedFragment.createBundle(it),
                setupFragmentTransaction = {
                    it.setSlideAnimation()
                }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null)
            childFragmentManager.commit {
                add(R.id.filterFragmentContainer, SearchFilterFragment::class.java, null)
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
        toolbar.apply {
            setNavigationOnClickListener {
                hideKeyboard()
                activity?.onBackPressed()
            }
        }
        ViewCompat.requestApplyInsets(coordinatorLayout)
    }

    override fun onResume() {
        super.onResume()
        disposeOnPause(
            viewModel.data.subscribe {
                wallpaperAdapter.submitData(lifecycle, it)
            }
        )
    }
}