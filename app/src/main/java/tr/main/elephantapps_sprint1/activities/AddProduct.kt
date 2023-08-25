package tr.main.elephantapps_sprint1.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tr.main.elephantapps_sprint1.Constants.Constans
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.databinding.ActivityAddProductBinding
import tr.main.elephantapps_sprint1.enums.EmailSender
import tr.main.elephantapps_sprint1.model.request.AddProduct.AdditionalInfoModel
import tr.main.elephantapps_sprint1.model.request.AddProduct.ProductAddModel
import tr.main.elephantapps_sprint1.model.request.UserModel
import tr.main.elephantapps_sprint1.util.Utils
import tr.main.elephantapps_sprint1.viewmodel.AddProductViewModel
import tr.main.elephantapps_sprint1.viewmodel.SigninAndLoginViewModel

class AddProduct : BaseActivity() {

    private lateinit var binding : ActivityAddProductBinding
    private var categoryName : String? = null
    private var brandName : String? = null

    private var productTitle : String? = null
    private var productCode : String? = null
    private var productDesc : String? = null
    private var productStatus : String? = null
    private var productIsOpenToOffer : Boolean? = false
    private var productStock : Int? = 0
    private var categoryId : Int? = 0
    private var brandId : Int? = 0

    private var productWidth : Int? = 0
    private var productHeight : Int? = 0
    private var productLength : Int? = 0
    private var productWeight : Int? = 0

    private var price : Int? = 0

    private var product : ProductAddModel? = null
    private var additionalInfo : AdditionalInfoModel? = null
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var addProductActivity: Activity
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarAddProduct)

        addProductActivity = this

        if (supportActionBar != null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarAddProduct.setNavigationOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.activity_left_to_right, R.anim.activity_right_to_left)
        }




        binding.toolbarAddProduct.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24)


        getProduct()



        binding.rgProductStatus.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {
                R.id.option_unused -> {
                    setValues(binding)
                    productStatus = "New"
                }
                R.id.option_used -> {
                    setValues(binding)
                    productStatus = "Used"
                }
            }
        }

        binding.rgIsOpenToOffer.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {
                R.id.option_open -> {
                    setValues(binding)
                    productIsOpenToOffer = true
                }
                R.id.option_close -> {
                    setValues(binding)
                    productIsOpenToOffer = false
                }
            }
        }



        binding.llCategory.setOnClickListener {

            setValues(binding)
            val intent = Intent(this@AddProduct,SearchCategory::class.java)
            intent.putExtra("product", product)
            intent.putExtra("additionalInfo",additionalInfo)
            startActivity(intent)
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
        }

        binding.llBrand.setOnClickListener {
            setValues(binding)
            val intent = Intent(this@AddProduct,SearchBrands::class.java)
            intent.putExtra("product", product)
            intent.putExtra("additionalInfo",additionalInfo)
            startActivity(intent)
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
        }

        binding.llCargoSize.setOnClickListener {
            setValues(binding)
            val intent = Intent(this@AddProduct,CargoSize::class.java)
            intent.putExtra("product", product)
            intent.putExtra("additionalInfo",additionalInfo)
            startActivity(intent)
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
        }

        binding.llPrice.setOnClickListener {
            setValues(binding)
            val intent = Intent(this@AddProduct,Price::class.java)
            intent.putExtra("product", product)
            intent.putExtra("additionalInfo",additionalInfo)
            startActivity(intent)
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
        }

        binding.btnContinue.setOnClickListener {
            checkValues(binding)
            if (checkValues(binding)){
                setValues(binding)
                println("butona tiklandi: $product")
                postProduct()

            }
        }


        binding.nested.setOnTouchListener { _, event ->
            if (event?.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }
        binding.toolbarAddProduct.setOnTouchListener { _, event ->
            if (event?.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }
    }
    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
    @SuppressLint("SetTextI18n")
    private fun getProduct(){
        product = intent.getParcelableExtra("product") as? ProductAddModel
        additionalInfo = intent.getParcelableExtra("additionalInfo") as? AdditionalInfoModel

        binding.tvProductTitle.text = product?.title?.toEditable()
        binding.etProductCode.text = product?.code?.toEditable()
        binding.tvProductDescription.text = product?.description?.toEditable()
        binding.etProductStock.text = product?.stock?.toString()?.toEditable()

        categoryId = product?.categoryId
        brandId = product?.brandId

        categoryName = additionalInfo?.categoryName
        brandName = additionalInfo?.brandName

        println("token : " + Utils.getUserAccessToken(this@AddProduct))

        binding.tvCategory.text = additionalInfo?.categoryName
        binding.tvBrand.text = additionalInfo?.brandName

        productStatus = product?.productStatus
        productIsOpenToOffer = product?.isOpenToOffer

        if (productStatus == "New"){
            binding.optionUnused.isChecked  = true
        }
        if (productStatus == "Used"){
            binding.optionUsed.isChecked  = true
        }
        if (productIsOpenToOffer == true){
            binding.optionOpen.isChecked  = true
        }
        if (productIsOpenToOffer == false){
            binding.optionClose.isChecked  = true
        }

        productWidth = product?.width
        productHeight = product?.height
        productLength = product?.length
        productWeight = product?.weight

        if (productWidth != null && productHeight != null && productLength != null ){
            binding.tvCargoSize.text = product?.width.toString() + "x" + product?.height.toString() + "x" + product?.length.toString()
        }

        price = product?.price

        if(price != null && price != 0){
            binding.tvPrice.text = Constans.TURKISH_LIRA_SYMBOL+price.toString()
        }

       //
       // println("getProduct() çalisti: $additionalInfo")
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun setValues(binding : ActivityAddProductBinding){

        productTitle = binding.tvProductTitle.text.toString()
        productCode = binding.etProductCode.text.toString()
        productDesc = binding.tvProductDescription.text.toString()

        val etProductStock = binding.etProductStock.text?.toString()?.trim()
        productStock = if (!etProductStock.isNullOrEmpty()) {
            etProductStock.toIntOrNull() ?: 0
        } else {
            0
        }


        if(categoryName != null){
            binding.tvCategory.text = categoryName
        }

        if(brandName != null){
            binding.tvBrand.text = brandName
        }


        product = ProductAddModel(
            title = productTitle,
            stock = productStock,
            code = productCode,
            description = productDesc,
            brandId = brandId,
            categoryId = categoryId,
            productStatus = productStatus,
            isOpenToOffer = productIsOpenToOffer,
            width = productWidth,
            height = productHeight,
            length = productLength,
            weight = productWeight,
            price = price,
            requestId = 0
        )


        additionalInfo = AdditionalInfoModel(
            categoryName = categoryName,
            brandName = brandName
        )
    }

    private fun checkValues(binding: ActivityAddProductBinding):Boolean{

        if (binding.tvProductTitle.text.isNullOrEmpty()){
            Toast.makeText(this,"Lütfen ürün başlığını giriniz.",Toast.LENGTH_SHORT).show()
            return false
        }else if (binding.etProductCode.text.isNullOrEmpty()){
            Toast.makeText(this,"Lütfen ürün kodunu giriniz.",Toast.LENGTH_SHORT).show()
            return false
        }else if (binding.tvProductDescription.text.isNullOrEmpty()){
            Toast.makeText(this,"Lütfen ürün açıklamasını giriniz.",Toast.LENGTH_SHORT).show()
            return false
        }else if (binding.etProductStock.text.isNullOrEmpty()){
            Toast.makeText(this,"Lütfen ürün stoğunu giriniz.",Toast.LENGTH_SHORT).show()
            return false
        }else if (binding.optionUsed.isChecked == false && binding.optionUnused.isChecked == false){
            Toast.makeText(this,"Lütfen ürün durumunu giriniz.",Toast.LENGTH_SHORT).show()
            return false
        }else if (binding.optionClose.isChecked == false && binding.optionOpen.isChecked == false){
            Toast.makeText(this,"Lütfen ürün teklif durumunu giriniz.",Toast.LENGTH_SHORT).show()
            return false
        }else if (binding.tvCategory.text.isNullOrEmpty()){
            Toast.makeText(this,"Lütfen ürün kategorisini giriniz.",Toast.LENGTH_SHORT).show()
            return false
        }else if (binding.tvBrand.text.isNullOrEmpty()){
            Toast.makeText(this,"Lütfen ürün markasını giriniz.",Toast.LENGTH_SHORT).show()
            return false
        }else if (binding.tvCategory.text.isNullOrEmpty()){
            Toast.makeText(this,"Lütfen kargo boyutunu giriniz.",Toast.LENGTH_SHORT).show()
            return false
        }else if (binding.tvCategory.text.isNullOrEmpty()){
            Toast.makeText(this,"Lütfen fiyat bilgisini giriniz.",Toast.LENGTH_SHORT).show()
            return false
        }else{
            return true
        }
    }

    private fun postProduct(){
        val viewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)

        viewModel.getDataFromAPI(product!!,this@AddProduct)

        viewModel.successLiveData.observe(this, Observer { success ->
            if (success == true) {
                viewModel.dataLiveData.observe(this, Observer { data ->
                    if (data != 0) {
                        val intent = Intent(this@AddProduct,AddPhoto::class.java)
                        intent.putExtra("data",data)
                        startActivity(intent)
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
                    }
                })
            }
        })

        viewModel.errorLiveData.observe(this,Observer{message->
            if (message != ""){
                Toast.makeText(this@AddProduct,message,Toast.LENGTH_LONG).show()
            }

        })
    }

}