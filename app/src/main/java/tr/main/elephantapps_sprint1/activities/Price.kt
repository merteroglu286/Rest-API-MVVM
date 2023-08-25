package tr.main.elephantapps_sprint1.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.addTextChangedListener
import tr.main.elephantapps_sprint1.Constants.Constans
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.databinding.ActivityPriceBinding
import tr.main.elephantapps_sprint1.model.request.AddProduct.AdditionalInfoModel
import tr.main.elephantapps_sprint1.model.request.AddProduct.ProductAddModel
import java.text.DecimalFormat

class Price : BaseActivity() {

    private lateinit var binding : ActivityPriceBinding
    private var receivedProduct : ProductAddModel? = null
    private var additionalInfo : AdditionalInfoModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPriceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()

        receivedProduct = intent.getParcelableExtra("product") as? ProductAddModel
        additionalInfo = intent.getParcelableExtra("additionalInfo") as? AdditionalInfoModel

        binding.etPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                calculatePrice()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.btnSave.setOnClickListener {
            calculatePrice()
            AddProduct.addProductActivity.finish()
            val intent = Intent(this@Price,AddProduct::class.java)
            intent.putExtra("product",receivedProduct)
            intent.putExtra("additionalInfo",additionalInfo)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }


    }

    private fun setToolbar(){
        setSupportActionBar(binding.toolbarPrice)

        if (supportActionBar != null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarPrice.setNavigationOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.activity_left_to_right, R.anim.activity_right_to_left)
        }

        binding.toolbarPrice.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24)

    }

    @SuppressLint("SetTextI18n")
    private fun calculatePrice() {
        val etPrice = binding.etPrice.text.toString().trim()
        val price = if (etPrice.isNotEmpty()) {
            etPrice.toDoubleOrNull() ?: 0.0
        } else {
            0.0
        }
        val turkishLiraSymbol = Constans.TURKISH_LIRA_SYMBOL

        val hizmetBedeli = (price * 0.10)
        val kazanc = price - hizmetBedeli

        if (hizmetBedeli != 0.0){
            binding.tvCargoPrice.text = etPrice

            val decimalFormat = DecimalFormat("0.00")
            val formattedhizmetBedeli = decimalFormat.format(hizmetBedeli)
            val formattedKazanc = decimalFormat.format(kazanc)

            val formattedhizmetBedeliWithoutTrailingZero = if (formattedhizmetBedeli.endsWith("0")) {
                formattedhizmetBedeli.dropLast(1)
            } else {
                formattedhizmetBedeli
            }

            val formattedKazancWithoutTrailingZero = if (formattedKazanc.endsWith("0")) {
                formattedKazanc.dropLast(1)
            } else {
                formattedKazanc
            }

            if (hizmetBedeli % 1 == 0.0) {
                binding.tvHizmetBedeli.text = turkishLiraSymbol + hizmetBedeli.toInt().toString()
                binding.tvKazanc.text = turkishLiraSymbol + kazanc.toInt().toString()
            } else {
                binding.tvHizmetBedeli.text = turkishLiraSymbol + formattedhizmetBedeliWithoutTrailingZero
                binding.tvKazanc.text = turkishLiraSymbol + formattedKazancWithoutTrailingZero
            }

            receivedProduct?.price = price.toInt()
        }else{
            binding.tvCargoPrice.text = ""
            binding.tvHizmetBedeli.text = ""
            binding.tvKazanc.text = ""
        }

    }

}