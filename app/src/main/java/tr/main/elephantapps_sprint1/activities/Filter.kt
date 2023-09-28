package tr.main.elephantapps_sprint1.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import tr.main.elephantapps_sprint1.Constants.Constans
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.adapter.BrandsAdapter
import tr.main.elephantapps_sprint1.adapter.SubCategoryXAdapter
import tr.main.elephantapps_sprint1.databinding.ActivityFilterBinding
import tr.main.elephantapps_sprint1.enums.ProductStatus
import tr.main.elephantapps_sprint1.model.request.AddProduct.AdditionalInfoModel
import tr.main.elephantapps_sprint1.model.request.AddProduct.ProductAddModel
import tr.main.elephantapps_sprint1.model.request.CategoryFilterModel
import tr.main.elephantapps_sprint1.model.request.PagingParameters
import tr.main.elephantapps_sprint1.model.request.SearchModel
import tr.main.elephantapps_sprint1.util.Utils
import tr.main.elephantapps_sprint1.viewmodel.CategoriesViewModel
import tr.main.elephantapps_sprint1.viewmodel.SearchBrandsViewModel

class Filter : BaseActivity() {

    private lateinit var binding : ActivityFilterBinding

    private var categoryName : String? = null
    private var brandName : String? = null
    private var minPrice : Int? = null
    private var maxPrice : Int? = null
    private var categoryId : Int? = null
    private var brandId : Int? = null
    private var garageId : Int? = null
    private var usingStatus : String? = null

    private var additionalInfo : AdditionalInfoModel? = null
    private var searchModel : SearchModel? = null

    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var sharedPreferencesCategoryAndBrandNames : SharedPreferences

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var filterActivity: Activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        filterActivity = this

        categoryId= intent.getIntExtra("categoryId",0)
        garageId= intent.getIntExtra("garageId",0)

        sharedPreferences = this.getSharedPreferences(Constans.FILTER_SHARED_PREFERENCE, Context.MODE_PRIVATE)
        sharedPreferencesCategoryAndBrandNames = this.getSharedPreferences(Constans.CATEGORY_AND_BRAND_NAMES_SHARED_PREFERENCE, Context.MODE_PRIVATE)

        clearFilter()

        binding.btnClose.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.activity_left_to_right, R.anim.activity_right_to_left)
        }


        binding.llCategory.setOnClickListener {
            setSearchModel()
            val intent = Intent(this@Filter, SearchCategory::class.java)
            intent.putExtra(Constans.FROM_WHICH_ACTIVITY,Constans.FROM_FILTER)
            startActivity(intent)
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
        }
        setSearchModel()

        binding.llBrand.setOnClickListener {
            setSearchModel()
            val intent = Intent(this@Filter, SearchBrands::class.java)
            intent.putExtra(Constans.FROM_WHICH_ACTIVITY,Constans.FROM_FILTER)
            startActivity(intent)
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
        }

        binding.llUsingStatus.setOnClickListener {
            binding.popupUsingStatus.visibility = View.VISIBLE

            binding.itemFirst.setOnClickListener {
                usingStatus = null
                binding.tvUsingStatus.text = binding.itemFirst.text.toString()
                binding.popupUsingStatus.visibility = View.GONE
            }

            binding.itemSecond.setOnClickListener {
                usingStatus = ProductStatus.Used.name
                binding.tvUsingStatus.text = binding.itemSecond.text.toString()
                binding.popupUsingStatus.visibility = View.GONE
            }

            binding.itemThird.setOnClickListener {
                usingStatus = ProductStatus.New.name
                binding.tvUsingStatus.text = binding.itemThird.text.toString()
                binding.popupUsingStatus.visibility = View.GONE
            }
        }

        binding.btnSave.setOnClickListener {
            setSearchModel()
            isFilter(true)
            finish()
            overridePendingTransition(R.anim.activity_left_to_right, R.anim.activity_right_to_left)
        }

        binding.txtClear.setOnClickListener {
            clearFilter()
            isFilter(false)
        }



    }

    override fun onResume() {
        super.onResume()

        val gson = Gson()

        val json = sharedPreferences.getString("SearchModel", "")

        val searchModel = gson.fromJson(json, SearchModel::class.java)

        if(searchModel.brandId != null){
            brandId = searchModel.brandId
            getBrandName(brandId!!)
        }

        if(searchModel.categoryId != null){
            categoryId = searchModel.categoryId
            getCategoryName(categoryId!!)
        }

    }

    private fun getBrandName(brandId : Int){
        val viewModel = ViewModelProvider(this).get(SearchBrandsViewModel::class.java)

        viewModel.getDataFromAPI(this@Filter)


        viewModel.brandsLiveData.observe(this, Observer { model->
            for (data in model.data){
                if(brandId == data.id){
                    binding.tvBrand.text  = data.name
                  break
                }
            }
        })
    }

    private fun getCategoryName(categoryId : Int){
        val viewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)

        val model = CategoryFilterModel(
            false
        )
        viewModel.getDataFromAPI(model)


        viewModel.categoriesLiveData.observe(this, Observer { model ->
            if (model.success) {

                for (data in model.data){
                    if (categoryId == data.id){
                        binding.tvCategory.text = data.name
                        break
                    }
                    for (subData in data.subCategories){
                        if (categoryId == subData.id){
                            binding.tvCategory.text = subData.name
                            break
                        }
                        for (subSubData in subData.subCategories){
                            if (categoryId == subSubData.id){
                                binding.tvCategory.text = subSubData.name
                                break
                            }
                        }
                    }
                }

            }
        })
    }

    private fun setSearchModel(){

        val etMinPrice = binding.etMinPrice.text?.toString()?.trim()
        minPrice = if (!etMinPrice.isNullOrEmpty()) {
            etMinPrice.toIntOrNull()
        } else {
            null
        }

        val etMaxPrice = binding.etMaxPrice.text?.toString()?.trim()
        maxPrice = if (!etMaxPrice.isNullOrEmpty()) {
            etMaxPrice.toIntOrNull()
        } else {
            null
        }


        val searchModel = SearchModel(
            minPrice = minPrice,
            maxPrice = maxPrice,
            categoryId = categoryId,
            brandId = brandId,
            garageId = garageId,
            productStatus = usingStatus,
            saleStatus = "OnSale",
            approvalStatus = "Approved",
            pagingParameters = PagingParameters(pageNumber = 1, pageSize = 50)
        )

        setSharedPreferences(searchModel)
    }

    private fun clearFilter(){

        val editor = sharedPreferences.edit()
        editor.remove("SearchModel")
        editor.apply()

        binding.tvCategory.text = ""
        binding.tvBrand.text = ""

        brandId = null

        binding.etMinPrice.text.clear()
        binding.etMaxPrice.text.clear()

        minPrice = null
        maxPrice = null

        binding.tvUsingStatus.text = "Kullanım Durumu"

        usingStatus = null
    }

    private fun isFilter(isFilter:Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean("isFilter",isFilter)
        editor.apply()
    }

    private fun setSharedPreferences(searchModel: SearchModel){
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json : String = gson.toJson(searchModel)
        editor.putString("SearchModel",json)
        editor.apply()
    }

}