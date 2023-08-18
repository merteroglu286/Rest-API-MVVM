package tr.main.elephantapps_sprint1.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import tr.main.elephantapps_sprint1.Constants.Constans
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.adapter.BrandsAdapter
import tr.main.elephantapps_sprint1.databinding.ActivityAddProductBinding
import tr.main.elephantapps_sprint1.databinding.DialogLayoutAddProductBinding
import tr.main.elephantapps_sprint1.model.request.ProductRequestModel
import tr.main.elephantapps_sprint1.viewmodel.AddProductViewModel
import tr.main.elephantapps_sprint1.viewmodel.SearchBrandsViewModel

class AddProduct : BaseActivity() {

    private lateinit var binding : ActivityAddProductBinding
    private var categoryName : String? = null
    private var brandName : String? = null
    private var categoryId : Int? = 0
    private var brandId : Int? = 0

    private lateinit var dialog: AlertDialog
    private lateinit var dialogBinding : DialogLayoutAddProductBinding
    private var success : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarAddProduct)

        if (supportActionBar != null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarAddProduct.setNavigationOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.activity_left_to_right, R.anim.activity_right_to_left)
        }



        binding.toolbarAddProduct.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24)

        categoryName = intent.getStringExtra(Constans.CATEGORY_NAME)
        brandName = intent.getStringExtra(Constans.BRAND_NAME)

        if (intent.getIntExtra(Constans.CATEGORY_ID,0) != 0){
            categoryId = intent.getIntExtra(Constans.CATEGORY_ID,0)
        }
        if (intent.getIntExtra(Constans.BRAND_ID,0) != 0){
            brandId = intent.getIntExtra(Constans.BRAND_ID,0)
        }


        if(categoryName != null){
            binding.tvCategory.text = categoryName
        }

        if(brandName != null){
            binding.tvBrand.text = brandName
        }


        binding.tvCategory.setOnClickListener {
            val intent = Intent(this@AddProduct,SearchCategory::class.java)
            intent.putExtra(Constans.BRAND_NAME,brandName)
            intent.putExtra(Constans.BRAND_ID,brandId)
            startActivity(intent)
        }

        binding.tvBrand.setOnClickListener {
            val intent = Intent(this@AddProduct,SearchBrands::class.java)
            intent.putExtra(Constans.CATEGORY_NAME,categoryName)
            intent.putExtra(Constans.CATEGORY_ID,categoryId)
            startActivity(intent)
        }

        binding.btnCreateRequest.setOnClickListener {

            val viewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)

            val productRequestModel = ProductRequestModel(
                brandId!!,
                categoryId!!,
                binding.tvProductDescription.text.toString(),
                binding.tvProductName.text.toString()

            )
            viewModel.getDataFromAPI(productRequestModel,this@AddProduct)

            viewModel.successLiveData.observe(this, Observer {success ->
                if(success){
                    val alertDialog = AlertDialog.Builder(this)
                    dialogBinding = DialogLayoutAddProductBinding.inflate(layoutInflater)
                    alertDialog.setView(dialogBinding.root)
                    dialog = alertDialog.create()
                    dialog.show()

                    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                    dialogBinding.btnReturnToHomepage.setOnClickListener {

                        startActivity(Intent(this@AddProduct,Dashboard::class.java))
                        finish()
                        dialog.dismiss()

                    }
                }else{
                    Toast.makeText(this,"hata", Toast.LENGTH_LONG).show()
                }
            })

            viewModel.errorLiveData.observe(this,Observer{message->
                if (message != ""){
                    Toast.makeText(this,message, Toast.LENGTH_LONG).show()
                }

            })

        }

    }


}