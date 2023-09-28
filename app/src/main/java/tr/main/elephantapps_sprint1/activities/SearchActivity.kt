package tr.main.elephantapps_sprint1.activities

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import tr.main.elephantapps_sprint1.adapter.BrandResultAdapter
import tr.main.elephantapps_sprint1.adapter.CategoryResultAdapter
import tr.main.elephantapps_sprint1.adapter.CategoryResultAdapterr
import tr.main.elephantapps_sprint1.adapter.GarageResultAdapter
import tr.main.elephantapps_sprint1.adapter.PopularSearchAdapter
import tr.main.elephantapps_sprint1.adapter.ProductResultAdapter
import tr.main.elephantapps_sprint1.databinding.ActivitySearchBinding
import tr.main.elephantapps_sprint1.model.request.HomeSearchRequestModel
import tr.main.elephantapps_sprint1.model.request.PagingParameters
import tr.main.elephantapps_sprint1.model.request.SearchModel
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
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.searchView, InputMethodManager.SHOW_IMPLICIT)

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
        }

        getHomeAllData()


    }

    private fun getSearchResult(newText:String?){
        val viewModel = ViewModelProvider(this).get(HomeSearchViewModel::class.java)

        if (newText != ""){
            val searchModel = SearchModel(
                pagingParameters = PagingParameters(pageSize = 50, pageNumber = 1),
                searchText = newText
            )
            viewModel.getDataFromAPI(searchModel)

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


                   println("categoryList : " +categoryList)
                   println("brandList : " +brandList)
                   println("garageList : " +garageList)
                   println("productList : " +productList)

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