package tr.main.elephantapps_sprint1.activities

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tr.main.elephantapps_sprint1.databinding.ActivityDashboardBinding
import tr.main.elephantapps_sprint1.fragments.Homepage
import tr.main.elephantapps_sprint1.fragments.Messages
import tr.main.elephantapps_sprint1.fragments.MyBasket
import tr.main.elephantapps_sprint1.fragments.MyGarage
import tr.main.elephantapps_sprint1.fragments.Profile
import tr.main.elephantapps_sprint1.model.response.Home.HomeAllResponseModel
import tr.main.elephantapps_sprint1.service.ApiService
import tr.main.elephantapps_sprint1.util.Constans
import tr.main.elephantapps_sprint1.util.Constans.Companion.BASE_URL

class Dashboard : BaseActivity(), OnClickListener {

    private lateinit var binding: ActivityDashboardBinding
    private var selectedContainerId = tr.main.elephantapps_sprint1.R.id.container_homepage

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
        fragmentTransaction.replace(tr.main.elephantapps_sprint1.R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun updateIconVisibility(selectedContainerId: Int) {
        binding.ivHome.visibility = if (selectedContainerId == tr.main.elephantapps_sprint1.R.id.container_homepage) View.GONE else View.VISIBLE
        binding.ivMessage.visibility = if (selectedContainerId == tr.main.elephantapps_sprint1.R.id.container_messages) View.GONE else View.VISIBLE
        binding.ivMyGarage.visibility = if (selectedContainerId == tr.main.elephantapps_sprint1.R.id.container_my_garage) View.GONE else View.VISIBLE
        binding.ivMyBasket.visibility = if (selectedContainerId == tr.main.elephantapps_sprint1.R.id.container_my_masket) View.GONE else View.VISIBLE
        binding.ivProfile.visibility = if (selectedContainerId == tr.main.elephantapps_sprint1.R.id.container_profile) View.GONE else View.VISIBLE

        binding.ivHomeFilled.visibility = if (selectedContainerId == tr.main.elephantapps_sprint1.R.id.container_homepage) View.VISIBLE else View.GONE
        binding.ivMessageFilled.visibility = if (selectedContainerId == tr.main.elephantapps_sprint1.R.id.container_messages) View.VISIBLE else View.GONE
        binding.ivMyGarageFilled.visibility = if (selectedContainerId == tr.main.elephantapps_sprint1.R.id.container_my_garage) View.VISIBLE else View.GONE
        binding.ivMyBasketFilled.visibility = if (selectedContainerId == tr.main.elephantapps_sprint1.R.id.container_my_masket) View.VISIBLE else View.GONE
        binding.ivProfileFilled.visibility = if (selectedContainerId == tr.main.elephantapps_sprint1.R.id.container_profile) View.VISIBLE else View.GONE
    }

    override fun onClick(v: View?) {
        val fragment: Fragment = when (v?.id) {
            tr.main.elephantapps_sprint1.R.id.container_homepage -> Homepage()
            tr.main.elephantapps_sprint1.R.id.container_messages -> Messages()
            tr.main.elephantapps_sprint1.R.id.container_my_garage -> MyGarage()
            tr.main.elephantapps_sprint1.R.id.container_my_masket -> MyBasket()
            tr.main.elephantapps_sprint1.R.id.container_profile -> Profile()
            else -> return
        }

        replaceFragment(fragment)
        selectedContainerId = v.id
        updateIconVisibility(selectedContainerId)
    }
}
