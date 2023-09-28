package tr.main.elephantapps_sprint1.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import tr.main.elephantapps_sprint1.Constants.Constans
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.adapter.SubCategoriesAdapter
import tr.main.elephantapps_sprint1.databinding.ActivitySearchSubCategoryBinding
import tr.main.elephantapps_sprint1.model.request.AddProduct.AdditionalInfoModel
import tr.main.elephantapps_sprint1.model.request.CategoryFilterModel
import tr.main.elephantapps_sprint1.model.request.AddProduct.ProductAddModel
import tr.main.elephantapps_sprint1.model.request.SearchModel
import tr.main.elephantapps_sprint1.model.response.Category.Data
import tr.main.elephantapps_sprint1.model.response.Category.SubCategory
import tr.main.elephantapps_sprint1.viewmodel.CategoriesViewModel
import java.util.Locale

class SearchSubCategory : BaseActivity() {

    private lateinit var binding : ActivitySearchSubCategoryBinding
    private val categoryList = ArrayList<Data>()
    private val subCategoryList = ArrayList<SubCategory>()

    private var subCategoriesAdapter: SubCategoriesAdapter? = null

    private var receivedProduct : ProductAddModel? = null
    private var additionalInfo : AdditionalInfoModel? = null

    private lateinit var fromWhichActivity : String
    private lateinit var sharedPreferences : SharedPreferences

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var searchSubCategoryActivity: Activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchSubCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        receivedProduct = intent.getParcelableExtra("product") as? ProductAddModel
        additionalInfo = intent.getParcelableExtra("additionalInfo") as? AdditionalInfoModel

        fromWhichActivity = intent.getStringExtra(Constans.FROM_WHICH_ACTIVITY) as String
        sharedPreferences = this.getSharedPreferences(Constans.FILTER_SHARED_PREFERENCE, Context.MODE_PRIVATE)

        createToolbar()
        getsubCategoryList()
        searchSubCategoryActivity = this

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                filterList(newText)
                return true
            }
        })


    }


    private fun createToolbar(){
        if (supportActionBar != null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarSearchCategory.setNavigationOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.activity_left_to_right, R.anim.activity_right_to_left)
        }

        binding.toolbarSearchCategory.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24)

        val queryHint = SpannableString("Kategori Ara")
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

    private fun filterList(query: String?) {

        if (query != null) {
            val filteredList = ArrayList<SubCategory>()
            for (i in subCategoryList) {
                if (i.name.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                filteredList.clear()
            } else {
                subCategoriesAdapter!!.setFilteredList(filteredList)
            }
        }
    }

    private fun setSharedPreferences(categoryId: Int){

        val gson = Gson()

        val json = sharedPreferences.getString("SearchModel", "")

        val searchModel = gson.fromJson(json, SearchModel::class.java)

        searchModel.categoryId = categoryId

        val updatedJson = gson.toJson(searchModel)

        val editor = sharedPreferences.edit()
        editor.putString("SearchModel", updatedJson)
        editor.apply()

    }

    private fun getsubCategoryList(){
        val viewModel = ViewModelProvider(this)[CategoriesViewModel::class.java]

        val model = CategoryFilterModel(
            false
        )
        viewModel.getDataFromAPI(model)


        viewModel.categoriesLiveData.observe(this, Observer { model->
            if (model.success){

                subCategoryList.clear()

                categoryList.clear()
                categoryList.addAll(model.data)

                val itemName = intent.getStringExtra("itemName")
                for (x in categoryList){
                    if(x.name == itemName){
                        subCategoryList.addAll(x.subCategories)
                    }

                }

                subCategoriesAdapter = SubCategoriesAdapter(subCategoryList) { data ->

                    if (data.subCategories.isEmpty()){
                        if (fromWhichActivity == Constans.FROM_FILTER){
                            setSharedPreferences(data.id)
                            SearchCategory.searchCategoryActivity.finish()
                            finish()
                            overridePendingTransition(R.anim.activity_left_to_right, R.anim.activity_right_to_left)
                        }else{
                            receivedProduct?.categoryId = data.id
                            additionalInfo?.categoryName = data.name
                            AddProduct.addProductActivity.finish()
                            SearchCategory.searchCategoryActivity.finish()
                            val intent = Intent(this@SearchSubCategory,AddProduct::class.java)
                            intent.putExtra("product",receivedProduct)
                            intent.putExtra("additionalInfo",additionalInfo)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                            finish()
                        }

                    }else{
                        val intent = Intent(this@SearchSubCategory,SearchSubCategoryX::class.java)
                        intent.putExtra("subName",data.name)
                        intent.putExtra("product",receivedProduct)
                        intent.putExtra("additionalInfo",additionalInfo)
                        intent.putExtra(Constans.FROM_WHICH_ACTIVITY,Constans.FROM_FILTER)
                        startActivity(intent)
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
                    }

                }
                binding.rvSubcategories.layoutManager = GridLayoutManager(this,3)
                binding.rvSubcategories.adapter = subCategoriesAdapter
            }
        })
    }
}