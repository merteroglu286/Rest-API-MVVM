package tr.main.elephantapps_sprint1.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.databinding.ActivityCarsoSizeBinding
import tr.main.elephantapps_sprint1.model.request.AddProduct.AdditionalInfoModel
import tr.main.elephantapps_sprint1.model.request.AddProduct.ProductAddModel

class CargoSize : BaseActivity() {

    private lateinit var binding : ActivityCarsoSizeBinding
    private var receivedProduct : ProductAddModel? = null
    private var additionalInfo : AdditionalInfoModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCarsoSizeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()

        receivedProduct = intent.getParcelableExtra("product") as? ProductAddModel
        additionalInfo = intent.getParcelableExtra("additionalInfo") as? AdditionalInfoModel

        binding.btnSave.setOnClickListener {
            setValues()
            AddProduct.addProductActivity.finish()
            val intent = Intent(this@CargoSize,AddProduct::class.java)
            intent.putExtra("product",receivedProduct)
            intent.putExtra("additionalInfo",additionalInfo)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        }

        binding.llWidth.setOnClickListener {
            focusAndShowKeyboard(binding.etWidth)
        }

        binding.llHeight.setOnClickListener {
            focusAndShowKeyboard(binding.etHeight)
        }

        binding.llLength.setOnClickListener {
            focusAndShowKeyboard(binding.etLength)
        }

        binding.llWeight.setOnClickListener {
            focusAndShowKeyboard(binding.etWeight)
        }
    }

    private fun focusAndShowKeyboard(editText: EditText) {
        editText.requestFocus()
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setToolbar(){
        setSupportActionBar(binding.toolbarCargoSize)

        if (supportActionBar != null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarCargoSize.setNavigationOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.activity_left_to_right, R.anim.activity_right_to_left)
        }

        binding.toolbarCargoSize.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24)

    }

    private fun setValues(){
        val etWidth = binding.etWidth.text?.toString()?.trim()
        val etHeight = binding.etHeight.text?.toString()?.trim()
        val etLength = binding.etLength.text?.toString()?.trim()
        val etWeight = binding.etWeight.text?.toString()?.trim()


        receivedProduct?.width = if (!etWidth.isNullOrEmpty()) {
            etWidth.toIntOrNull() ?: 0
        } else {
            0
        }

        receivedProduct?.height = if (!etHeight.isNullOrEmpty()) {
            etHeight.toIntOrNull() ?: 0
        } else {
            0
        }

        receivedProduct?.length = if (!etLength.isNullOrEmpty()) {
            etLength.toIntOrNull() ?: 0
        } else {
            0
        }

        receivedProduct?.weight = if (!etWeight.isNullOrEmpty()) {
            etWeight.toIntOrNull() ?: 0
        } else {
            0
        }
    }

}
