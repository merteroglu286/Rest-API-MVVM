package tr.main.elephantapps_sprint1.activities

import android.app.Dialog
import android.content.Intent
import android.hardware.display.VirtualDisplay
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.databinding.ActivityForgotPasswordBinding
import tr.main.elephantapps_sprint1.model.request.SendVerificationCodeModel
import tr.main.elephantapps_sprint1.model.request.UserLoginModel
import tr.main.elephantapps_sprint1.model.request.VerifyCodeModel
import tr.main.elephantapps_sprint1.model.response.ResponseModel
import tr.main.elephantapps_sprint1.service.ApiService
import tr.main.elephantapps_sprint1.util.Constans
import tr.main.elephantapps_sprint1.util.Constans.Companion.API_KEY
import tr.main.elephantapps_sprint1.util.Constans.Companion.BASE_URL
import tr.main.elephantapps_sprint1.util.EmailSender
import tr.main.elephantapps_sprint1.util.Utils
import tr.main.elephantapps_sprint1.viewmodel.ForgotPasswordViewModel
import tr.main.elephantapps_sprint1.viewmodel.SigninAndLoginViewModel
import tr.main.elephantapps_sprint1.viewmodel.VerifyCodeViewModel

class ForgotPassword : AppCompatActivity() {

    private lateinit var binding : ActivityForgotPasswordBinding
    private lateinit var customProgressDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customProgressDialog = Dialog(this)
        customProgressDialog.setContentView(R.layout.dialog_custom_progress)
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.setCancelable(false)

        setSupportActionBar(binding.toolbarMailVerification)

        if (supportActionBar != null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarMailVerification.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.toolbarMailVerification.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24)

        binding.txtGiveUp.setOnClickListener {
            onBackPressed()
        }

        binding.btnSend.setOnClickListener {
            sendCode()
        }
    }

    private fun sendCode(){
        customProgressDialog.show()
        val viewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)

        viewModel.callApiForVerificationCode(binding.etEmail.text.toString())

        viewModel.successLiveData.observe(this, Observer { success ->
            customProgressDialog.dismiss()
            if (success == true) {
                val intent = Intent(this@ForgotPassword,MailVerification::class.java)
                intent.putExtra("email_sender", EmailSender.ForgotPasswordActivity)
                intent.putExtra("email",binding.etEmail.text.toString())
                startActivity(intent)
                finish()
            }
        })

        viewModel.errorLiveData.observe(this,Observer{message->
            customProgressDialog.dismiss()
            if (message != ""){
                Toast.makeText(this@ForgotPassword,message,Toast.LENGTH_LONG).show()
            }

        })
    }

}