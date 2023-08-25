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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.adapter.BrandsAdapter
import tr.main.elephantapps_sprint1.databinding.ActivitySearchBrandsBinding
import tr.main.elephantapps_sprint1.model.request.AddProduct.AdditionalInfoModel
import tr.main.elephantapps_sprint1.model.request.AddProduct.ProductAddModel
import tr.main.elephantapps_sprint1.model.response.Brand.Data
import tr.main.elephantapps_sprint1.viewmodel.SearchBrandsViewModel
import java.util.Locale

class SearchBrands : BaseActivity() {

    private lateinit var binding : ActivitySearchBrandsBinding
    private var success : Boolean = false
    private val brandList = ArrayList<Data>()
    private lateinit var brandsAdapter : BrandsAdapter
    private var receivedProduct : ProductAddModel? = null
    private var additionalInfo : AdditionalInfoModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBrandsBinding.inflate(layoutInflater)
        setContentView(binding.root)


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

        println(receivedProduct)

        createToolbar()
        getBrandList()
    }


    private fun filterList(query: String?) {

        if (query != null) {
            val filteredList = ArrayList<Data>()
            for (i in brandList) {
                if (i.name.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                filteredList.clear()
            } else {
                brandsAdapter.setFilteredList(filteredList)
            }
        }
    }

    private fun getBrandList(){
        val viewModel = ViewModelProvider(this).get(SearchBrandsViewModel::class.java)

        viewModel.getDataFromAPI(this@SearchBrands)

        viewModel.successLiveData.observe(this, Observer {success ->
            this.success = success
        })

        viewModel.errorLiveData.observe(this,Observer{message->
            if (message != ""){
                Toast.makeText(this,message, Toast.LENGTH_LONG).show()
            }

        })



        viewModel.brandsLiveData.observe(this, Observer { model->
            if (success){

                brandList.clear()
                brandList.addAll(model.data)


                brandsAdapter = BrandsAdapter(brandList) { data ->
                    receivedProduct?.brandId = data.id
                    additionalInfo?.brandName = data.name
                    AddProduct.addProductActivity.finish()
                    val intent = Intent(this@SearchBrands,AddProduct::class.java)
                    intent.putExtra("product",receivedProduct)
                    intent.putExtra("additionalInfo",additionalInfo)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
                    finish()
                }
                binding.rvBrands.layoutManager = GridLayoutManager(this,3)
                binding.rvBrands.adapter = brandsAdapter
            }
        })
    }

    private fun createToolbar(){
        if (supportActionBar != null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarSearchBrand.setNavigationOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.activity_left_to_right, R.anim.activity_right_to_left)
        }

        binding.toolbarSearchBrand.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24)

        val queryHint = SpannableString("Marka Ara")
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

}