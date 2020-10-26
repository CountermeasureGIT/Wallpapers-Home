package ru.countermeasure.wallpapershome.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_main.*
import ru.countermeasure.wallpapershome.R
import ru.countermeasure.wallpapershome.ui.latest.LatestListFragment
import ru.countermeasure.wallpapershome.ui.random.RandomListFragment
import ru.countermeasure.wallpapershome.ui.toplist.TopListFragment

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

    class MainFragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> TopListFragment.newInstance()
            1 -> LatestListFragment()
            else -> RandomListFragment()
        }
    }
}