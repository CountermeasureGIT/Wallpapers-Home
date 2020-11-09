package ru.countermeasure.wallpapershome.presentation.main

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_main.*
import ru.countermeasure.wallpapershome.R
import ru.countermeasure.wallpapershome.presentation.latest.LatestListFragment
import ru.countermeasure.wallpapershome.presentation.random.RandomListFragment
import ru.countermeasure.wallpapershome.presentation.search.SearchFragment
import ru.countermeasure.wallpapershome.presentation.toplist.TopListFragment
import ru.countermeasure.wallpapershome.utils.navigateTo
import ru.countermeasure.wallpapershome.utils.setSlideAnimation

class MainFragment : Fragment(R.layout.fragment_main) {

    private val titles by lazy {
        arrayListOf(
            getString(R.string.toplist_screen_label),
            getString(R.string.latest_screen_label),
            getString(R.string.random_screen_label)
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewPager.adapter = MainFragmentStateAdapter(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_search) {
                activity?.supportFragmentManager?.navigateTo(SearchFragment::class.java) {
                    it.setSlideAnimation()
                }
                return@setOnMenuItemClickListener true
            }
            return@setOnMenuItemClickListener false
        }
        ViewCompat.requestApplyInsets(coordinatorLayout)
    }

    class MainFragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> TopListFragment.newInstance()
            1 -> LatestListFragment()
            else -> RandomListFragment()
        }
    }
}