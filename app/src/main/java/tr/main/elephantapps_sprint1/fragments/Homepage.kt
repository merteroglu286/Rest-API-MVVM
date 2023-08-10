package tr.main.elephantapps_sprint1.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.activities.SearchActivity
import tr.main.elephantapps_sprint1.adapter.CategoryAdapter
import tr.main.elephantapps_sprint1.adapter.FeaturedGaragesAdapter
import tr.main.elephantapps_sprint1.adapter.FeaturedProductsAdapter
import tr.main.elephantapps_sprint1.databinding.FragmentHomepaegBinding
import tr.main.elephantapps_sprint1.model.request.HomeSearchRequestModel
import tr.main.elephantapps_sprint1.model.request.PagingParameters
import tr.main.elephantapps_sprint1.model.request.UserModel
import tr.main.elephantapps_sprint1.model.response.Home.Category
import tr.main.elephantapps_sprint1.model.response.Home.Garages
import tr.main.elephantapps_sprint1.model.response.Home.Product
import tr.main.elephantapps_sprint1.viewmodel.HomeSearchViewModel
import tr.main.elephantapps_sprint1.viewmodel.HomeViewModel

class Homepage : Fragment() {

    private lateinit var binding : FragmentHomepaegBinding
    private var success : Boolean = false
    private val categoryList = ArrayList<Category>()
    private val garageList = ArrayList<Garages>()
    private val productList = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomepaegBinding.inflate(layoutInflater,container,false)
        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getHomeAllData()

        binding.searchBar.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                val intent = Intent(v.context, SearchActivity::class.java)
                v.context.startActivity(intent)
                binding.searchBar.clearFocus()
            }
        }
    }
    private fun getHomeAllData(){
        val viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        viewModel.getDataFromAPI()

        viewModel.successLiveData.observe(viewLifecycleOwner, Observer {success ->
            this.success = success
        })

        viewModel.errorLiveData.observe(viewLifecycleOwner,Observer{message->
            if (message != ""){
                Toast.makeText(this.context,message,Toast.LENGTH_LONG).show()
            }

        })

        viewModel.homeAllLiveData.observe(viewLifecycleOwner, Observer { model->
            if (success){

                categoryList.clear()
                categoryList.addAll(model.data.categories)

                val categoryAdapter = CategoryAdapter(categoryList)
                binding.rvCategories.layoutManager = GridLayoutManager(this.context,3)
                binding.rvCategories.adapter = categoryAdapter


                garageList.clear()
                garageList.addAll(model.data.garages)
                garageList.shuffle()

                val garageAdapter = FeaturedGaragesAdapter(garageList)
                binding.rvFeaturedGarages.layoutManager = GridLayoutManager(this.context,5)
                binding.rvFeaturedGarages.adapter = garageAdapter


                productList.clear()
                productList.addAll(model.data.products)
                productList.shuffle()

                val productAdapter = FeaturedProductsAdapter(productList)
                binding.rvFeaturedProducts.layoutManager = GridLayoutManager(this.context,3)
                binding.rvFeaturedProducts.adapter = productAdapter
            }
        })
    }
}