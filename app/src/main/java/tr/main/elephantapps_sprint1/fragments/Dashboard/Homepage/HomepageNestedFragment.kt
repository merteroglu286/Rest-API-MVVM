package tr.main.elephantapps_sprint1.fragments.Dashboard.Homepage

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import tr.main.elephantapps_sprint1.Constants.Constans
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.activities.Dashboard
import tr.main.elephantapps_sprint1.activities.LoginAndSignin
import tr.main.elephantapps_sprint1.adapter.CategoryAdapter
import tr.main.elephantapps_sprint1.adapter.FeaturedGaragesAdapter
import tr.main.elephantapps_sprint1.adapter.FeaturedProductsAdapter
import tr.main.elephantapps_sprint1.adapter.UrgentProductsAdapter
import tr.main.elephantapps_sprint1.databinding.FragmentHomepageNestedBinding
import tr.main.elephantapps_sprint1.enums.ToastType
import tr.main.elephantapps_sprint1.model.response.Home.Category
import tr.main.elephantapps_sprint1.model.response.Home.Garages
import tr.main.elephantapps_sprint1.model.response.Home.Product
import tr.main.elephantapps_sprint1.model.response.Home.UrgentProduct
import tr.main.elephantapps_sprint1.util.Utils
import tr.main.elephantapps_sprint1.viewmodel.HomeViewModel

class HomepageNestedFragment : Fragment() {

    private lateinit var binding : FragmentHomepageNestedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomepageNestedBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getHomeAllData()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun getHomeAllData(){
        val viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        viewModel.getDataFromAPI()

        viewModel.errorLiveData.observe(viewLifecycleOwner) { message ->
            if (message != "") {
                Utils.showToast(requireContext(),message,Toast.LENGTH_SHORT,ToastType.Red)
            }

        }
        viewModel.categories.observe(viewLifecycleOwner){data->
            val categories = data as ArrayList<Category>
            val categoryAdapter = CategoryAdapter(categories,requireActivity())
            binding.rvCategories.layoutManager = GridLayoutManager(this.context, 3)
            binding.rvCategories.adapter = categoryAdapter
        }

        viewModel.garages.observe(viewLifecycleOwner){data->
            val garages = data as ArrayList<Garages>
            garages.shuffle()
            val garageAdapter = FeaturedGaragesAdapter(garages,requireActivity())
            binding.rvFeaturedGarages.layoutManager = GridLayoutManager(this.context, 5)
            binding.rvFeaturedGarages.adapter = garageAdapter
        }

        viewModel.products.observe(viewLifecycleOwner){data->
            val products = data as ArrayList<Product>
            products.shuffle()
            val productAdapter = FeaturedProductsAdapter(products,requireActivity())
            binding.rvFeaturedProducts.layoutManager = GridLayoutManager(this.context, 3)
            binding.rvFeaturedProducts.adapter = productAdapter
        }

        viewModel.lastVideo.observe(viewLifecycleOwner){data->
            binding.tvLastVideo.text = data.title
            val videoUrl = data.videoUrl
            val videoId = extractVideoIdFromUrl(videoUrl)

            if (videoId != null){
                Glide.with(this).load("https://img.youtube.com/vi/$videoId/maxresdefault.jpg").into(binding.ivLastVideo)
            }
            binding.ivLastVideo.setOnClickListener {
                val webSettings: WebSettings = binding.wvLastVideo.settings
                webSettings.javaScriptEnabled = true
                binding.wvLastVideo.loadUrl(data.videoUrl)
            }
        }

        viewModel.randomGlossary.observe(viewLifecycleOwner){data->
            binding.txtGlossaryName.text = data.name
            binding.txtGlossaryDescription.text = data.description

            //Glide.with(this).load(data.photoUrl).into(binding.ivGlossary)
        }

        viewModel.urgentProducts.observe(viewLifecycleOwner){data->

            val urgentProducts = data as ArrayList<UrgentProduct>
            val productAdapter = UrgentProductsAdapter(urgentProducts,requireActivity())
            binding.rvUrgentProducts.layoutManager = GridLayoutManager(this.context, 3)
            binding.rvUrgentProducts.adapter = productAdapter

        }

        viewModel.lastAnnouncement.observe(viewLifecycleOwner){data->

            binding.tvAnnouncementName.text = data.name
            binding.tvAnnouncementDescription.text = data.description
            val buttonUrl = data.buttonUrl
            binding.btnSeeAnnouncement.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(buttonUrl))
                startActivity(intent)
            }
        }

        viewModel.lastEvent.observe(viewLifecycleOwner){data->

            binding.tvLastEventName.text = data.name
            binding.tvLastEventDescription.text = data.description
            binding.tvLastEventDate.text = data.date
            val buttonUrl = data.buttonUrl
            binding.btnSeeLastEvent.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(buttonUrl))
                startActivity(intent)
            }
        }

    }

    private fun extractVideoIdFromUrl(url: String): String? {
        val pattern = "v=([\\w-]+)"
        val regex = Regex(pattern)
        val matchResult = regex.find(url)
        return matchResult?.groupValues?.get(1)
    }

}