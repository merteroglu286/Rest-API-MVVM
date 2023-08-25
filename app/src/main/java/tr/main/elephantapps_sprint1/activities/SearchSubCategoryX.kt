package tr.main.elephantapps_sprint1.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.adapter.SubCategoryXAdapter
import tr.main.elephantapps_sprint1.databinding.ActivitySearchSubCategoryXBinding
import tr.main.elephantapps_sprint1.model.request.AddProduct.AdditionalInfoModel
import tr.main.elephantapps_sprint1.model.request.CategoryFilterModel
import tr.main.elephantapps_sprint1.model.request.AddProduct.ProductAddModel
import tr.main.elephantapps_sprint1.model.response.Category.Data
import tr.main.elephantapps_sprint1.model.response.Category.SubCategoryX
import tr.main.elephantapps_sprint1.viewmodel.CategoriesViewModel
import java.util.Locale

class SearchSubCategoryX : BaseActivity() {

    private lateinit var binding : ActivitySearchSubCategoryXBinding
    private val categoryList = ArrayList<Data>()
    private val subCategoryXList = ArrayList<SubCategoryX>()

    private var subCategoryXAdapter: SubCategoryXAdapter? = null
    private var success : Boolean = false

    private var receivedProduct : ProductAddModel? = null
    private var additionalInfo : AdditionalInfoModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchSubCategoryXBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createToolbar()

        getCategoryList()
        getSubCategoryXList()

        receivedProduct = intent.getParcelableExtra("product") as? ProductAddModel
        additionalInfo = intent.getParcelableExtra("additionalInfo") as? AdditionalInfoModel

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
            val filteredList = ArrayList<SubCategoryX>()
            for (i in subCategoryXList) {
                if (i.name.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                filteredList.clear()
            } else {
                subCategoryXAdapter!!.setFilteredList(filteredList)
            }
        }
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
                Toast.makeText(this,message, Toast.LENGTH_LONG).show()
            }

        })

        viewModel.categoriesLiveData.observe(this, Observer { model->
            if (success){
                categoryList.clear()
                for (list in model.data) {
                    categoryList.add(list)
                }
            }
        })
    }


    private fun getSubCategoryXList() {
        val viewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)

        val model = CategoryFilterModel(
            false
        )
        viewModel.getDataFromAPI(model)


        viewModel.categoriesLiveData.observe(this, Observer { model ->
            if (model.success) {
                subCategoryXList.clear()
                categoryList.clear()

                categoryList.addAll(model.data)

                val itemName = intent.getStringExtra("subName")

                for (data in categoryList) {
                    for (subCategory in data.subCategories) {
                        if (subCategory.name == itemName) {
                            subCategoryXList.addAll(subCategory.subCategories)
                            break
                        }
                    }
                }

                subCategoryXAdapter = SubCategoryXAdapter(subCategoryXList) { data ->
                    receivedProduct?.categoryId = data.id
                    additionalInfo?.categoryName = data.name
                    SearchCategory.searchCategoryActivity.finish()
                    SearchSubCategory.searchSubCategoryActivity.finish()
                    AddProduct.addProductActivity.finish()
                    val intent = Intent(this@SearchSubCategoryX, AddProduct::class.java)
                    intent.putExtra("product",receivedProduct)
                    intent.putExtra("additionalInfo",additionalInfo)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    finish()
                }

                binding.rvSubcategories.layoutManager = GridLayoutManager(this, 3)
                binding.rvSubcategories.adapter = subCategoryXAdapter
            }
        })
    }

}