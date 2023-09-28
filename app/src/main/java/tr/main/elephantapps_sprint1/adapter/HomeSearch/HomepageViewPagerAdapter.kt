package tr.main.elephantapps_sprint1.adapter.HomeSearch

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import tr.main.elephantapps_sprint1.fragments.Dashboard.Homepage.HomepageNestedFragment
import tr.main.elephantapps_sprint1.fragments.Dashboard.Homepage.Search.HomepageSearchFragment

class HomepageViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                HomepageNestedFragment()
            }
            1 -> {
                HomepageSearchFragment()
            }
            else -> {
                Fragment()
            }

        }
    }
}