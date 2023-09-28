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
import android.view.animation.AnimationUtils
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
import tr.main.elephantapps_sprint1.databinding.ActivityFilterProductBinding
import tr.main.elephantapps_sprint1.enums.OrderByProduct
import tr.main.elephantapps_sprint1.model.request.PagingParameters
import tr.main.elephantapps_sprint1.model.request.SearchModel
import tr.main.elephantapps_sprint1.model.response.Search.Product
import tr.main.elephantapps_sprint1.viewmodel.FilterProductViewModel

class FilterProduct : BaseActivity() {

    private lateinit var binding : ActivityFilterProductBinding
    private val productList = ArrayList<Product>()
    private lateinit var sharedPreferences : SharedPreferences
    private var categoryId : Int? = null
    private var orderByProduct: OrderByProduct = OrderByProduct.NewToOld


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFilterProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showProgress()

        sharedPreferences = this.getSharedPreferences(Constans.FILTER_SHARED_PREFERENCE, Context.MODE_PRIVATE)

        categoryId= intent.getIntExtra("categoryId",0)

        getProductByCategory(categoryId!!,orderByProduct,null)

        isFilter(false)

        binding.ivBack.setOnClickListener{
            onBackPressed()
            overridePendingTransition(R.anim.activity_left_to_right, R.anim.activity_right_to_left)
        }

        createSearchBar()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.isNullOrEmpty()){
                    getProductByCategory(categoryId!!,orderByProduct,newText)
                }else{
                    getProductByCategory(categoryId!!,orderByProduct,newText)
                }
                return false
            }

        })

        binding.btnFilter.setOnClickListener {
            val intent = Intent(this@FilterProduct,Filter::class.java)
            intent.putExtra("categoryId",categoryId)
            startActivity(intent)
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
        }

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

        setupTextViewClickListeners(layouts, imageViews)
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

    private fun setupTextViewClickListeners(layouts: List<LinearLayout>, imageViews: List<ImageView>) {
        for (i in layouts.indices) {
            layouts[i].setOnClickListener {
                showProgress()
                when(i){
                    0 -> {
                        orderByProduct = OrderByProduct.NewToOld
                        getProductByCategory(categoryId!!,orderByProduct,null)
                        binding.popupSort.visibility = View.GONE
                    }
                    1 -> {
                        orderByProduct = OrderByProduct.PriceHighToLow
                        getProductByCategory(categoryId!!,orderByProduct,null)
                        binding.popupSort.visibility = View.GONE
                    }
                    2 -> {
                        orderByProduct = OrderByProduct.PriceLowToHigh
                        getProductByCategory(categoryId!!,orderByProduct,null)
                        binding.popupSort.visibility = View.GONE
                    }
                    3 -> {
                        orderByProduct = OrderByProduct.NewToOld
                        getProductByCategory(categoryId!!,orderByProduct,null)
                        binding.popupSort.visibility = View.GONE
                    }
                    4 -> {
                        orderByProduct = OrderByProduct.OldToNew
                        getProductByCategory(categoryId!!,orderByProduct,null)
                        binding.popupSort.visibility = View.GONE
                    }
                }
                for (j in imageViews.indices) {
                    imageViews[j].visibility = if (i == j) View.VISIBLE else View.INVISIBLE

                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        showProgress()
        getProductByCategory(categoryId!!,orderByProduct,null)
    }

    private fun isFilter(isFilter:Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean("isFilter",isFilter)
        editor.apply()
    }

    private fun getIsFilter(): Boolean {
        return sharedPreferences.getBoolean("isFilter", false)
    }


    private fun getProductByCategory(categoryId: Int,orderByProduct: OrderByProduct,searchText : String?){
        val viewModel = ViewModelProvider(this).get(FilterProductViewModel::class.java)

        if(getIsFilter() == false){
            Toast.makeText(this@FilterProduct,"false",Toast.LENGTH_SHORT).show()

            val model = SearchModel(
                approvalStatus = "Approved",
                pagingParameters = PagingParameters(pageNumber = 1, pageSize = 50),
                saleStatus = "OnSale",
                categoryId = categoryId,
                orderBy = orderByProduct,
                searchText = searchText
            )
            viewModel.getDataFromAPI(model)


            viewModel.errorLiveData.observe(this,Observer{message->
                if (message != ""){
                    Toast.makeText(this@FilterProduct,message, Toast.LENGTH_LONG).show()
                }

            })

            viewModel.productLiveData.observe(this, Observer { model->
                if (model.success){

                    productList.clear()

                    if (model.data?.products != null){
                        productList.addAll(model.data.products)
                    }

                    productList.reverse()
                    val productAdapter = FilterSProductAdapter(productList,this@FilterProduct)
                    binding.rvProduct.layoutManager = GridLayoutManager(this@FilterProduct,3)
                    binding.rvProduct.adapter = productAdapter

                    removeProgress()
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
                orderBy = orderByProduct

            )
            viewModel.getDataFromAPI(model)

            viewModel.errorLiveData.observe(this,Observer{message->
                if (message != ""){
                    Toast.makeText(this@FilterProduct,message, Toast.LENGTH_LONG).show()
                }

            })

            viewModel.productLiveData.observe(this, Observer { model->
                if (model.success){

                    productList.clear()

                    if (model.data?.products != null){
                        productList.addAll(model.data.products)
                    }

                    productList.reverse()
                    val productAdapter = FilterSProductAdapter(productList,this@FilterProduct)
                    binding.rvProduct.layoutManager = GridLayoutManager(this@FilterProduct,3)
                    binding.rvProduct.adapter = productAdapter

                    removeProgress()
                }
            })
        }

    }

    private fun getSearchModelFromSharedPreference(): SearchModel {
        val gson = Gson()
        val json = sharedPreferences.getString("SearchModel", "")
        return gson.fromJson(json, SearchModel::class.java)
    }

    private fun showProgress(){
        binding.rvProduct.visibility = View.GONE
        binding.rlProgress.visibility = View.VISIBLE

        val rotateAnimation = AnimationUtils.loadAnimation(this@FilterProduct, R.anim.rotate_anim)

        binding.ivProgress.startAnimation(rotateAnimation)
    }

    private fun removeProgress(){
        binding.rvProduct.visibility = View.VISIBLE
        binding.rlProgress.visibility = View.GONE
        binding.ivProgress.clearAnimation()
    }


}