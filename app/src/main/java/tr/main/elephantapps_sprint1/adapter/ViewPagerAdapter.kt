package tr.main.elephantapps_sprint1.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import tr.main.elephantapps_sprint1.fragments.LoginAndSignin.Login
import tr.main.elephantapps_sprint1.fragments.LoginAndSignin.Signin

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                Signin()
            }
            1 -> {
                Login()
            }
            else -> {
                Fragment()
            }

        }
    }
}