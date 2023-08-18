package tr.main.elephantapps_sprint1.activities

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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import tr.main.elephantapps_sprint1.Constants.Constans
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.adapter.SearchCategoryAdapter
import tr.main.elephantapps_sprint1.adapter.SubCategoriesAdapter
import tr.main.elephantapps_sprint1.databinding.ActivitySearchCategoryBinding
import tr.main.elephantapps_sprint1.enums.SearchCategoryType
import tr.main.elephantapps_sprint1.model.request.CategoryFilterModel
import tr.main.elephantapps_sprint1.model.response.Category.Data
import tr.main.elephantapps_sprint1.model.response.Category.SubCategory
import tr.main.elephantapps_sprint1.model.response.Home.Category
import tr.main.elephantapps_sprint1.viewmodel.CategoriesViewModel
import java.util.Locale


class SearchCategory : BaseActivity() {

    private lateinit var binding : ActivitySearchCategoryBinding
    private val categoryList = ArrayList<Data>()
    private val subCategoryList = ArrayList<SubCategory>()

    private var subCategoriesAdapter: SubCategoriesAdapter? = null
    private var searchCategoriesAdapter: SearchCategoryAdapter? = null

    private var success : Boolean = false
    private var brandName : String? = null
    private var brandId : Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        brandName = intent.getStringExtra(Constans.BRAND_NAME)
        brandId = intent.getIntExtra(Constans.BRAND_ID,0)



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

        getCategoryList()
        getsubCategoryList()


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText.isNullOrBlank()) {
                    getCategoryList()
                    binding.rvSubcategories.visibility = View.GONE
                    binding.rvCategories.visibility = View.VISIBLE
                } else {
                    filterList(newText)
                    binding.rvCategories.visibility = View.GONE
                    binding.rvSubcategories.visibility = View.VISIBLE
                }
                return true
            }
        })



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

    private fun getsubCategoryList(){
        val viewModel = ViewModelProvider(this)[CategoriesViewModel::class.java]

        val model = CategoryFilterModel(
            false
        )
        viewModel.getDataFromAPI(model)

        viewModel.successLiveData.observe(this, Observer {success ->
            this.success = success
        })

        viewModel.errorLiveData.observe(this,Observer{message->
            if (message != ""){
                Toast.makeText(this,message,Toast.LENGTH_LONG).show()
            }

        })

        viewModel.categoriesLiveData.observe(this, Observer { model->
            if (success){

                subCategoryList.clear()

                for (x in categoryList){
                    subCategoryList.addAll(x.subCategories)
                }

                if(categoryList.isNotEmpty()){
                    subCategoriesAdapter = SubCategoriesAdapter(subCategoryList){data->
                       //println(data.name)
                    }
                    binding.rvSubcategories.layoutManager = GridLayoutManager(this,3)
                    binding.rvSubcategories.adapter = subCategoriesAdapter
                }
            }
        })
    }

    private fun getCategoryList(){
        val viewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)

        val model = CategoryFilterModel(
            false
        )
        viewModel.getDataFromAPI(model)

        viewModel.successLiveData.observe(this, Observer {success ->
            this.success = success
        })

        viewModel.errorLiveData.observe(this,Observer{message->
            if (message != ""){
                Toast.makeText(this,message,Toast.LENGTH_LONG).show()
            }

        })

        viewModel.categoriesLiveData.observe(this, Observer { model->
            if (success){

                categoryList.clear()
                for (list in model.data) {
                    categoryList.add(list)
                }

                searchCategoriesAdapter = SearchCategoryAdapter(categoryList) { data ->
                    val intent = Intent(this@SearchCategory,SearchSubCategory::class.java)
                    intent.putExtra("itemName",data.name)
                    if (brandName != null){
                        intent.putExtra(Constans.BRAND_NAME,brandName)
                    }
                    if (brandId != 0){
                        intent.putExtra(Constans.BRAND_ID,brandId)
                    }
                    startActivity(intent)
                }
                binding.rvCategories.layoutManager = GridLayoutManager(this,3)
                binding.rvCategories.adapter = searchCategoriesAdapter
            }
        })
    }

}