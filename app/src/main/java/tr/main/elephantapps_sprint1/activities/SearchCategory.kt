package tr.main.elephantapps_sprint1.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.adapter.SearchCategoryAdapter
import tr.main.elephantapps_sprint1.adapter.SearchCategoryAllAdapter
import tr.main.elephantapps_sprint1.databinding.ActivitySearchCategoryBinding
import tr.main.elephantapps_sprint1.model.request.AddProduct.AdditionalInfoModel
import tr.main.elephantapps_sprint1.model.request.CategoryFilterModel
import tr.main.elephantapps_sprint1.model.request.AddProduct.ProductAddModel
import tr.main.elephantapps_sprint1.model.response.Category.Data
import tr.main.elephantapps_sprint1.model.response.Category.SubCategory
import tr.main.elephantapps_sprint1.model.response.Category.SubCategoryX
import tr.main.elephantapps_sprint1.viewmodel.CategoriesViewModel
import java.util.Locale


class SearchCategory : BaseActivity() {

    private lateinit var binding : ActivitySearchCategoryBinding
    private val categoryList = ArrayList<Data>()
    private val subCategoryList = ArrayList<SubCategory>()
    private val subCategoryXList = ArrayList<SubCategoryX>()
    private val categoryNameList = ArrayList<String>()

    private var searchCategoriesAdapter: SearchCategoryAdapter? = null
    private var searchCategoryAllAdapter: SearchCategoryAllAdapter? = null

    private var receivedProduct : ProductAddModel? = null
    private var additionalInfo : AdditionalInfoModel? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var searchCategoryActivity: Activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()
        editSearchView()


        receivedProduct = intent.getParcelableExtra("product") as? ProductAddModel
        additionalInfo = intent.getParcelableExtra("additionalInfo") as? AdditionalInfoModel

        println(receivedProduct)
        getData()

        searchCategoryActivity = this

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText.isNullOrBlank()){
                    binding.rvAllCategories.visibility = View.GONE
                    binding.rvCategories.visibility = View.VISIBLE
                } else {
                    filterList(newText)
                    binding.rvCategories.visibility = View.GONE
                    binding.rvAllCategories.visibility = View.VISIBLE
                }
                return true
            }
        })



    }

    private fun editSearchView(){
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
    private fun setToolbar(){
        setSupportActionBar(binding.toolbarSearchCategory)

        if (supportActionBar != null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarSearchCategory.setNavigationOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.activity_left_to_right, R.anim.activity_right_to_left)
        }

        binding.toolbarSearchCategory.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24)

    }

    private fun filterList(query: String?) {

        if (query != null) {
            val filteredList = ArrayList<String>()
            for (i in categoryNameList) {
                if (i.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                filteredList.clear()
            } else {
                searchCategoryAllAdapter!!.setFilteredList(filteredList)
            }
        }
    }


    private fun getData(){
        val viewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)

        val model = CategoryFilterModel(
            false
        )
        viewModel.getDataFromAPI(model)

        viewModel.categoriesLiveData.observe(this, Observer { model->

            if (model.success){
                categoryList.clear()
                processData(model.data)

            }else{
                Toast.makeText(this,model.message,Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun processData(dataList: List<Data>) {
        categoryList.clear()
        subCategoryList.clear()
        subCategoryXList.clear()
        categoryNameList.clear()

        for (data in dataList) {
            categoryNameList.add(data.name)
            categoryList.add(data)

            for (subCategory in data.subCategories) {

                categoryNameList.add(subCategory.name)

                subCategoryList.add(subCategory)

                for (subCategoryX in subCategory.subCategories) {

                    categoryNameList.add(subCategoryX.name)
                    subCategoryXList.add(subCategoryX)

                }
            }
        }
        setSearchCategoryAdapter(categoryList)
        setSearchCategoryAllAdapter(categoryNameList,categoryList)

    }

    private fun setSearchCategoryAllAdapter(categoryNameList:ArrayList<String>,categoryList: ArrayList<Data>){
        searchCategoryAllAdapter = SearchCategoryAllAdapter(categoryNameList) { name ->
            for (data in categoryList) {

                if (data.name == name){
                    val intent = Intent(this@SearchCategory,SearchSubCategory::class.java)
                    intent.putExtra("itemName",data.name)
                    intent.putExtra("product",receivedProduct)
                    intent.putExtra("additionalInfo",additionalInfo)
                    startActivity(intent)
                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
                    break
                }

                for (subCategory in data.subCategories) {

                    if (subCategory.name == name && subCategory.subCategories.isNotEmpty()){
                        val intent = Intent(this@SearchCategory,SearchSubCategoryX::class.java)
                        intent.putExtra("subName",subCategory.name)
                        intent.putExtra("product",receivedProduct)
                        intent.putExtra("additionalInfo",additionalInfo)
                        startActivity(intent)
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
                        break

                    }else{
                        if(subCategory.name == name){
                            receivedProduct?.categoryId = subCategory.id
                            additionalInfo?.categoryName = subCategory.name
                            AddProduct.addProductActivity.finish()
                            val intent = Intent(this@SearchCategory, AddProduct::class.java)
                            intent.putExtra("product",receivedProduct)
                            intent.putExtra("additionalInfo",additionalInfo)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                            finish()
                            break
                        }
                    }

                    for (subCategoryX in subCategory.subCategories) {

                        if (subCategoryX.name == name){
                            receivedProduct?.categoryId = subCategoryX.id
                            additionalInfo?.categoryName = subCategoryX.name
                            AddProduct.addProductActivity.finish()
                            val intent = Intent(this@SearchCategory,AddProduct::class.java)
                            intent.putExtra("product",receivedProduct)
                            intent.putExtra("additionalInfo",additionalInfo)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                            finish()
                            break
                        }

                    }
                }
            }
        }

        binding.rvAllCategories.layoutManager = GridLayoutManager(this,3)
        binding.rvAllCategories.adapter = searchCategoryAllAdapter
    }
    private fun setSearchCategoryAdapter(categoryList:ArrayList<Data>){
        searchCategoriesAdapter = SearchCategoryAdapter(categoryList) { data ->
            val intent = Intent(this@SearchCategory,SearchSubCategory::class.java)
            intent.putExtra("product",receivedProduct)
            intent.putExtra("additionalInfo",additionalInfo)
            intent.putExtra("itemName",data.name)
            startActivity(intent)
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
        }

        binding.rvCategories.layoutManager = GridLayoutManager(this,3)
        binding.rvCategories.adapter = searchCategoriesAdapter
    }
}