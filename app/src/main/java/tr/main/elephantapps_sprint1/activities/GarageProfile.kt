package tr.main.elephantapps_sprint1.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import tr.main.elephantapps_sprint1.Constants.Constans
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.adapter.FilterSProductAdapter
import tr.main.elephantapps_sprint1.adapter.GarageProductsByGarageProfileAdapter
import tr.main.elephantapps_sprint1.adapter.GarageProductsByProductDetailAdapter
import tr.main.elephantapps_sprint1.databinding.ActivityGarageProfileBinding
import tr.main.elephantapps_sprint1.enums.OrderByProduct
import tr.main.elephantapps_sprint1.model.request.GetProductByGarage.Data
import tr.main.elephantapps_sprint1.model.request.PagingParameters
import tr.main.elephantapps_sprint1.model.request.SearchModel
import tr.main.elephantapps_sprint1.model.response.Search.Product
import tr.main.elephantapps_sprint1.viewmodel.FilterProductViewModel
import tr.main.elephantapps_sprint1.viewmodel.GarageProfileViewModel
import tr.main.elephantapps_sprint1.viewmodel.ProductDetailViewModel

class GarageProfile : BaseActivity() {
    private lateinit var binding : ActivityGarageProfileBinding
    private val productList = ArrayList<Product>()
    private lateinit var sharedPreferences : SharedPreferences
    private var orderByProduct: OrderByProduct = OrderByProduct.NewToOld
    private var garageId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGarageProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences(Constans.FILTER_SHARED_PREFERENCE, Context.MODE_PRIVATE)

        garageId = intent.getIntExtra("garageId",0)

        binding.ivBack.setOnClickListener{
            onBackPressed()
            overridePendingTransition(R.anim.activity_left_to_right, R.anim.activity_right_to_left)
        }

        isFilter(false)

        val productId = intent.getIntExtra("productId",0)
        getProductData(productId)
        createSearchBar()


        binding.btnSort.setOnClickListener {

            if (binding.popupSort.visibility == View.VISIBLE){
                binding.popupSort.visibility = View.INVISIBLE
            }else{
                binding.popupSort.visibility = View.VISIBLE
            }

        }

        val layouts = listOf(
            binding.optionDefault,
            binding.optionHighToLow,
            binding.optionLowToHigh,
            binding.optionNewToOld,
            binding.optionOldToNew
        )
        val imageViews = listOf(
            binding.ivCheckDefault,
            binding.ivCheckHighToLow,
            binding.ivCheckLowToHigh,
            binding.ivCheckNewToOld,
            binding.ivCheckOldToNew
        )
        getProductByGarageId(garageId,OrderByProduct.NewToOld,null)
        setupTextViewClickListeners(layouts, imageViews,garageId)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.isNullOrEmpty()){
                    getProductByGarageId(garageId,orderByProduct,null)
                }else{
                    getProductByGarageId(garageId,orderByProduct,newText)
                }
                return false
            }

        })

        binding.btnFilter.setOnClickListener {
            val intent = Intent(this@GarageProfile,Filter::class.java)
            intent.putExtra("garageId",garageId)
            startActivity(intent)
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
        }

    }

    override fun onResume() {
        super.onResume()
        getProductByGarageId(garageId,orderByProduct,null)
    }

    private fun createSearchBar(){

        val queryHint = SpannableString("Ürün / Ürün Kodu Ara")
        queryHint.setSpan(
            AbsoluteSizeSpan(14, true),
            0,
            queryHint.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        queryHint.setSpan(
            ForegroundColorSpan(Color.WHITE),
            0,
            queryHint.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.searchView.queryHint = queryHint
    }


    private fun setupTextViewClickListeners(layouts: List<LinearLayout>, imageViews: List<ImageView>,garageId: Int) {
        for (i in layouts.indices) {
            layouts[i].setOnClickListener {
                when(i){
                    0 -> {
                        orderByProduct = OrderByProduct.NewToOld
                        getProductByGarageId(garageId,orderByProduct,null)
                        binding.popupSort.visibility = View.GONE
                    }
                    1 -> {
                        orderByProduct = OrderByProduct.PriceHighToLow
                        getProductByGarageId(garageId,orderByProduct,null)
                        binding.popupSort.visibility = View.GONE
                    }
                    2 -> {
                        orderByProduct = OrderByProduct.PriceLowToHigh
                        getProductByGarageId(garageId,orderByProduct,null)
                        binding.popupSort.visibility = View.GONE
                    }
                    3 -> {
                        orderByProduct = OrderByProduct.NewToOld
                        getProductByGarageId(garageId,orderByProduct,null)
                        binding.popupSort.visibility = View.GONE
                    }
                    4 -> {
                        orderByProduct = OrderByProduct.OldToNew
                        getProductByGarageId(garageId,orderByProduct,null)
                        binding.popupSort.visibility = View.GONE
                    }
                }
                for (j in imageViews.indices) {
                    imageViews[j].visibility = if (i == j) View.VISIBLE else View.INVISIBLE

                }
            }
        }
    }

    private fun getProductData(id : Int){
        val viewModel = ViewModelProvider(this)[ProductDetailViewModel::class.java]

        viewModel.getDataFromAPI(id)

        viewModel.successLiveData.observe(this, Observer { success ->
            if (success == false){
                viewModel.errorLiveData.observe(this, Observer { error ->
                    Toast.makeText(this@GarageProfile, "Hata: $error", Toast.LENGTH_LONG).show()

                })
            }
        })

        viewModel.garageLiveData.observe(this, Observer { garage ->
            binding.tvGarageName.text = garage.userName
            binding.toolbarGarageName.text = garage.userName
            binding.followerCount.text = garage.followerCount.toString()
            binding.productAdvertCount.text = garage.productCount.toString()
            binding.productSoldCount.text = garage.soldProducts.toString()
            binding.txtScore.text = garage.score.toString()
            binding.tvGarageType.text = garage.garageType
        })

    }

    private fun getProductByGarageId(garageId: Int,orderByProduct: OrderByProduct,searchText:String?){
        val viewModel = ViewModelProvider(this).get(FilterProductViewModel::class.java)

        if(getIsFilter() == false) {
            Toast.makeText(this@GarageProfile, "false", Toast.LENGTH_SHORT).show()

            val model = SearchModel(
                approvalStatus = "Approved",
                pagingParameters = PagingParameters(pageNumber = 1, pageSize = 50),
                saleStatus = "OnSale",
                garageId = garageId,
                orderBy = orderByProduct,
                searchText = searchText
            )

            viewModel.getDataFromAPI(model)

            viewModel.errorLiveData.observe(this,Observer{message->
                if (message != ""){
                    Toast.makeText(this@GarageProfile,message, Toast.LENGTH_LONG).show()
                }

            })

            viewModel.productLiveData.observe(this, Observer { model->
                if (model.success){

                    productList.clear()

                    if (model.data?.products != null){
                        productList.addAll(model.data.products)
                    }

                    productList.reverse()
                    val productAdapter = FilterSProductAdapter(productList,this@GarageProfile)
                    binding.rvProducts.layoutManager = GridLayoutManager(this@GarageProfile,3)
                    binding.rvProducts.adapter = productAdapter
                }
            })
        }else{

            val shared = getSearchModelFromSharedPreference()

            val model = SearchModel(
                approvalStatus = "Approved",
                pagingParameters = PagingParameters(pageNumber = 1, pageSize = 50),
                saleStatus = "OnSale",
                categoryId = shared.categoryId,
                brandId = shared.brandId,
                minPrice = shared.minPrice,
                maxPrice = shared.maxPrice,
                productStatus = shared.productStatus,
                garageId = garageId,
                orderBy = orderByProduct

            )
            viewModel.getDataFromAPI(model)
            viewModel.errorLiveData.observe(this,Observer{message->
                if (message != ""){
                    Toast.makeText(this@GarageProfile,message, Toast.LENGTH_LONG).show()
                }

            })

            viewModel.productLiveData.observe(this, Observer { model->
                if (model.success){

                    productList.clear()

                    if (model.data?.products != null){
                        productList.addAll(model.data.products)
                    }

                    productList.reverse()
                    val productAdapter = FilterSProductAdapter(productList,this@GarageProfile)
                    binding.rvProducts.layoutManager = GridLayoutManager(this@GarageProfile,3)
                    binding.rvProducts.adapter = productAdapter
                }
            })

        }

    }

    private fun isFilter(isFilter:Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean("isFilter",isFilter)
        editor.apply()
    }

    private fun getIsFilter(): Boolean {
        return sharedPreferences.getBoolean("isFilter", false)
    }

    private fun getSearchModelFromSharedPreference(): SearchModel {
        val gson = Gson()
        val json = sharedPreferences.getString("SearchModel", "")
        return gson.fromJson(json, SearchModel::class.java)
    }
/*
    private fun getGarageData(garageId:Int){
        val viewModel = ViewModelProvider(this).get(GarageProfileViewModel::class.java)

        viewModel.getDataFromAPI(garageId)

        viewModel.successLiveData.observe(this, Observer { success ->
            if (success == false){
                viewModel.errorLiveData.observe(this, Observer { error ->
                    Toast.makeText(this@GarageProfile, "Hata: $error", Toast.LENGTH_LONG).show()

                })
            }
        })

        viewModel.data.observe(this, Observer { data ->
            val dataList = ArrayList<Data>()
            for (product in data){
                if(product.saleStatus == "OnSale"){
                    dataList.add(product)
                }
            }
            val productAdapter = GarageProductsByGarageProfileAdapter(dataList,this@GarageProfile)
            binding.rvProducts.layoutManager = GridLayoutManager(this@GarageProfile,3)
            binding.rvProducts.adapter = productAdapter
        })
    }

 */
}