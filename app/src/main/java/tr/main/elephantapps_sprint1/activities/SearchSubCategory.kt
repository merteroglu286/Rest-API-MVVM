package tr.main.elephantapps_sprint1.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import tr.main.elephantapps_sprint1.Constants.Constans
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.adapter.SearchCategoryAdapter
import tr.main.elephantapps_sprint1.adapter.SubCategoriesAdapter
import tr.main.elephantapps_sprint1.databinding.ActivitySearchCategoryBinding
import tr.main.elephantapps_sprint1.databinding.ActivitySearchSubCategoryBinding
import tr.main.elephantapps_sprint1.model.request.CategoryFilterModel
import tr.main.elephantapps_sprint1.model.response.Category.Data
import tr.main.elephantapps_sprint1.model.response.Category.SubCategory
import tr.main.elephantapps_sprint1.viewmodel.CategoriesViewModel

class SearchSubCategory : BaseActivity() {

    private lateinit var binding : ActivitySearchSubCategoryBinding
    private val categoryList = ArrayList<Data>()
    private val subCategoryList = ArrayList<SubCategory>()

    private var subCategoriesAdapter: SubCategoriesAdapter? = null
    private var success : Boolean = false
    private var brandName : String? = null
    private var brandId : Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchSubCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        brandName = intent.getStringExtra(Constans.BRAND_NAME)
        brandId = intent.getIntExtra(Constans.BRAND_ID,0)



        createToolbar()

        getCategoryList()
        getsubCategoryList()
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

                val itemName = intent.getStringExtra("itemName")
                for (x in categoryList){
                    if(x.name == itemName){
                        subCategoryList.addAll(x.subCategories)
                    }

                }

                subCategoriesAdapter = SubCategoriesAdapter(subCategoryList) { data ->
                    val intent = Intent(this@SearchSubCategory,AddProduct::class.java)
                    intent.putExtra(Constans.CATEGORY_NAME,data.name)
                    intent.putExtra(Constans.CATEGORY_ID,data.id)

                    if (brandName != null){
                        intent.putExtra(Constans.BRAND_NAME,brandName)
                    }
                    if (brandId != 0){
                        intent.putExtra(Constans.BRAND_ID,brandId)
                    }
                    startActivity(intent)
                    finish()

                    // parent.name verisine asagıdan ulasabilirsin
                    /*
                    for (x in categoryList){
                        if (x.id == data.parentId){
                            println(x.name + " - " + data.name)
                        }
                    }

                     */
                }
                binding.rvSubcategories.layoutManager = GridLayoutManager(this,3)
                binding.rvSubcategories.adapter = subCategoriesAdapter
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
            }
        })
    }
}