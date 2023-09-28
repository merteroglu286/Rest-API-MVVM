package tr.main.elephantapps_sprint1.activities

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.Fragment
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.databinding.ActivityDashboardBinding
import tr.main.elephantapps_sprint1.fragments.Dashboard.Homepage.Homepage
import tr.main.elephantapps_sprint1.fragments.Dashboard.Messages
import tr.main.elephantapps_sprint1.fragments.Dashboard.MyBasket
import tr.main.elephantapps_sprint1.fragments.Dashboard.MyGarage
import tr.main.elephantapps_sprint1.fragments.Dashboard.Profile

class Dashboard : BaseActivity(), OnClickListener {

    private lateinit var binding: ActivityDashboardBinding
    private var selectedContainerId = R.id.container_homepage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(Homepage())

        binding.containerHomepage.setOnClickListener(this)
        binding.containerMessages.setOnClickListener(this)
        binding.containerMyGarage.setOnClickListener(this)
        binding.containerMyMasket.setOnClickListener(this)
        binding.containerProfile.setOnClickListener(this)

        updateIconVisibility(selectedContainerId)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun updateIconVisibility(selectedContainerId: Int) {
        binding.ivHome.visibility = if (selectedContainerId == R.id.container_homepage) View.GONE else View.VISIBLE
        binding.ivMessage.visibility = if (selectedContainerId == R.id.container_messages) View.GONE else View.VISIBLE
        binding.ivMyGarage.visibility = if (selectedContainerId == R.id.container_my_garage) View.GONE else View.VISIBLE
        binding.ivMyBasket.visibility = if (selectedContainerId == R.id.container_my_masket) View.GONE else View.VISIBLE
        binding.ivProfile.visibility = if (selectedContainerId == R.id.container_profile) View.GONE else View.VISIBLE

        binding.ivHomeFilled.visibility = if (selectedContainerId == R.id.container_homepage) View.VISIBLE else View.GONE
        binding.ivMessageFilled.visibility = if (selectedContainerId == R.id.container_messages) View.VISIBLE else View.GONE
        binding.ivMyGarageFilled.visibility = if (selectedContainerId == R.id.container_my_garage) View.VISIBLE else View.GONE
        binding.ivMyBasketFilled.visibility = if (selectedContainerId == R.id.container_my_masket) View.VISIBLE else View.GONE
        binding.ivProfileFilled.visibility = if (selectedContainerId == R.id.container_profile) View.VISIBLE else View.GONE
    }

    override fun onClick(v: View?) {
        val fragment: Fragment = when (v?.id) {
            R.id.container_homepage -> Homepage()
            R.id.container_messages -> Messages()
            R.id.container_my_garage -> MyGarage()
            R.id.container_my_masket -> MyBasket()
            R.id.container_profile -> Profile()
            else -> return
        }

        replaceFragment(fragment)
        selectedContainerId = v.id
        updateIconVisibility(selectedContainerId)
    }
}
