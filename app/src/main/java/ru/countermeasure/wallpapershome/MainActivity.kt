package ru.countermeasure.wallpapershome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.main_activity.*
import ru.countermeasure.wallpapershome.ui.latest.LatestListFragment
import ru.countermeasure.wallpapershome.ui.random.RandomListFragment
import ru.countermeasure.wallpapershome.ui.toplist.TopListFragment

class MainActivity : AppCompatActivity() {

    private val fragmentStateAdapter = object : FragmentStateAdapter(this) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> TopListFragment.newInstance()
            1 -> LatestListFragment()
            else -> RandomListFragment()
        }
    }

    private val titles by lazy {
        arrayListOf(
            getString(R.string.toplist_screen_label),
            getString(R.string.latest_screen_label),
            getString(R.string.random_screen_label)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        rootToolbar.apply {
            setOnMenuItemClickListener {
                return@setOnMenuItemClickListener false
            }
        }

        viewPager.adapter = fragmentStateAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }
}