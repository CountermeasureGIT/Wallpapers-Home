package ru.countermeasure.wallpapershome.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.main_fragment.*
import ru.countermeasure.wallpapershome.R

class MainFragment : Fragment(R.layout.main_fragment) {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()
    private val wallpaperAdapter by lazy { WallpaperAdapter() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        wallpapersRecyclerView.apply {
            adapter = wallpaperAdapter
            layoutManager = GridLayoutManager(context, 3)
        }
    }

    override fun onResume() {
        super.onResume()
        with(viewModel) {
            data.subscribe {
                wallpaperAdapter.submitList(it)
            }
            loading.subscribe {

            }
        }
    }
}