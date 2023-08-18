package tr.main.elephantapps_sprint1.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.adapter.BrandResultAdapter
import tr.main.elephantapps_sprint1.adapter.CategoryResultAdapter
import tr.main.elephantapps_sprint1.adapter.GarageResultAdapter
import tr.main.elephantapps_sprint1.adapter.PopularSearchAdapter
import tr.main.elephantapps_sprint1.adapter.ProductResultAdapter
import tr.main.elephantapps_sprint1.databinding.ActivitySearchBinding
import tr.main.elephantapps_sprint1.model.request.HomeSearchRequestModel
import tr.main.elephantapps_sprint1.model.request.PagingParameters
import tr.main.elephantapps_sprint1.model.response.Home.PopularSearch
import tr.main.elephantapps_sprint1.model.response.Search.Brand
import tr.main.elephantapps_sprint1.model.response.Search.Category
import tr.main.elephantapps_sprint1.model.response.Search.Garage
import tr.main.elephantapps_sprint1.model.response.Search.Product
import tr.main.elephantapps_sprint1.viewmodel.HomeSearchViewModel
import tr.main.elephantapps_sprint1.viewmodel.HomeViewModel


class SearchActivity : BaseActivity() {
    private lateinit var binding : ActivitySearchBinding
    private var success : Boolean = false

    private val categoryList = ArrayList<Category>()
    private val garageList = ArrayList<Garage>()
    private val brandList = ArrayList<Brand>()
    private val productList = ArrayList<Product>()
    private val popularSearchList = ArrayList<PopularSearch>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivitySearchBinding.inflate(layoutInflater)


        setContentView(binding.root)

        binding.searchView.requestFocus()

        binding.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                getSearchResult(newText)
                return false
            }

        })

        binding.ivBack.setOnClickListener{
            onBackPressed()
            overridePendingTransition(R.anim.activity_left_to_right, R.anim.activity_right_to_left)
        }

        binding.btnCreateRequest.setOnClickListener {
            startActivity(Intent(this@SearchActivity,AddProduct::class.java))
        }
        getHomeAllData()


    }

    private fun getSearchResult(newText:String?){
        val viewModel = ViewModelProvider(this).get(HomeSearchViewModel::class.java)

        if (newText != ""){
            val homeSearchRequestModel = HomeSearchRequestModel(
                "Approved",
                PagingParameters(1,50),
                "OnSale",
                newText!!
            )

            viewModel.getDataFromAPI(homeSearchRequestModel)

            viewModel.successLiveData.observe(this, Observer {success ->
                this.success = success
            })

            viewModel.errorLiveData.observe(this,Observer{message->
                if (message != "" && message != "timeout"){
                    Toast.makeText(this,message, Toast.LENGTH_LONG).show()
                    println(message)
                }

            })

            viewModel.homeSearchLiveData.observe(this, Observer { model->
                if (success){
                    categoryList.clear()
                    brandList.clear()
                    garageList.clear()
                    productList.clear()

                    binding.txt.text = "İlgili Sonuçlar"
                    binding.rvPopularSearch.visibility = View.GONE
                    binding.nested.visibility = View.VISIBLE

                    model.data?.categories?.let { categoryList.addAll(it) }
                    model.data?.brands?.let { brandList.addAll(it) }
                    model.data?.garages?.let { garageList.addAll(it) }
                    model.data?.products?.let { productList.addAll(it) }


                    if (categoryList.isEmpty() && brandList.isEmpty() && garageList.isEmpty() && productList.isEmpty()){
                        binding.llEmptySearch.visibility = View.VISIBLE
                        binding.rvPopularSearch.visibility = View.GONE
                        binding.nested.visibility = View.GONE
                        binding.txt.visibility = View.GONE
                    }else{
                        binding.nested.visibility = View.VISIBLE
                        binding.llEmptySearch.visibility = View.GONE
                        binding.txt.visibility = View.VISIBLE
                        if(categoryList.isNotEmpty()){
                            val categoryAdapter = CategoryResultAdapter(categoryList)
                            binding.rvCategoryResult.layoutManager = GridLayoutManager(this,1)
                            binding.rvCategoryResult.adapter = categoryAdapter
                        }

                        if (brandList.isNotEmpty()){
                            val brandAdapter = BrandResultAdapter(brandList)
                            binding.rvBrandResult.layoutManager = GridLayoutManager(this,1)
                            binding.rvBrandResult.adapter = brandAdapter
                        }

                        if (garageList.isNotEmpty()){
                            val garageAdapter = GarageResultAdapter(garageList)
                            binding.rvGarageResult.layoutManager = GridLayoutManager(this,1)
                            binding.rvGarageResult.adapter = garageAdapter
                        }
                        if (productList.isNotEmpty()){
                            val productAdapter = ProductResultAdapter(productList)
                            binding.rvProductResult.layoutManager = GridLayoutManager(this,3)
                            binding.rvProductResult.adapter = productAdapter
                        }
                    }


                }
            })
        }else{
            getHomeAllData()
            categoryList.clear()
            brandList.clear()
            garageList.clear()
            productList.clear()
        }


    }

    private fun getHomeAllData(){
        val viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        viewModel.getDataFromAPI()

        viewModel.successLiveData.observe(this, Observer {success ->
            this.success = success
        })

        viewModel.errorLiveData.observe(this,Observer{message->
            if (message != ""){
                Toast.makeText(this,message,Toast.LENGTH_LONG).show()
            }

        })

        viewModel.homeAllLiveData.observe(this , Observer { model->
            if (success){

                popularSearchList.clear()
                popularSearchList.addAll(model.data.popularSearch)

                val categoryAdapter = PopularSearchAdapter(popularSearchList)
                binding.rvPopularSearch.layoutManager = GridLayoutManager(this,3)
                binding.rvPopularSearch.adapter = categoryAdapter

            }
        })

        binding.rvPopularSearch.visibility = View.VISIBLE
        binding.nested.visibility = View.GONE
        binding.txt.text = "Popüler Aramalar"
    }
}