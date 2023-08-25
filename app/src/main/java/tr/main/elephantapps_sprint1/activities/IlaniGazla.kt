package tr.main.elephantapps_sprint1.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.databinding.ActivityIlaniGazlaBinding
import tr.main.elephantapps_sprint1.databinding.DialogLayoutAddProductBinding

class IlaniGazla : AppCompatActivity() {

    private lateinit var binding : ActivityIlaniGazlaBinding
    private lateinit var dialog: AlertDialog
    private lateinit var dialogBinding : DialogLayoutAddProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIlaniGazlaBinding.inflate(layoutInflater)

        setContentView(binding.root)
        createToolbar()

        binding.txtDontWant.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            dialogBinding = DialogLayoutAddProductBinding.inflate(layoutInflater)
            alertDialog.setView(dialogBinding.root)
            dialog = alertDialog.create()
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
            dialog.show()

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialogBinding.btnReturnToHomepage.setOnClickListener {
                AddProduct.addProductActivity.finish()
                AddPhoto.addPhotoActivity.finish()
                startActivity(Intent(this@IlaniGazla,Dashboard::class.java))
                finish()
                dialog.dismiss()

            }
        }
    }

    private fun createToolbar(){
        setSupportActionBar(binding.toolbarIlaniGazla)

        if (supportActionBar != null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarIlaniGazla.setNavigationOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.activity_left_to_right, R.anim.activity_right_to_left)
        }

        binding.toolbarIlaniGazla.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24)

    }
}