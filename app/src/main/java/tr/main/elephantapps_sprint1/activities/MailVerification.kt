package tr.main.elephantapps_sprint1.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.databinding.ActivityMailVerificationBinding
import tr.main.elephantapps_sprint1.model.request.VerifyCodeModel
import tr.main.elephantapps_sprint1.enums.EmailSender
import tr.main.elephantapps_sprint1.viewmodel.VerifyCodeViewModel

class MailVerification : BaseActivity() {

    private lateinit var binding: ActivityMailVerificationBinding
    private lateinit var email: String
    private lateinit var customProgressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMailVerificationBinding.inflate(layoutInflater)
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
            overridePendingTransition(R.anim.activity_left_to_right, R.anim.activity_right_to_left)
        }

        binding.toolbarMailVerification.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24)

        binding.txtSendCodeAgain.setOnClickListener{
            Toast.makeText(this,"Kod yeniden gönderilecek",Toast.LENGTH_SHORT).show()
        }

        email = intent.getStringExtra("email").toString()

        binding.btnSend.setOnClickListener {
            callApiForVerifyCode()
        }

    }

    private fun callApiForVerifyCode(){
        customProgressDialog.show()
        val viewModel = ViewModelProvider(this).get(VerifyCodeViewModel::class.java)

        val verifyCodeModel = VerifyCodeModel(
            email,
            binding.pinview.text.toString()
        )


        viewModel.getStatusCodeForVerify(verifyCodeModel)

        viewModel.successVerifyLiveData.observe(this, Observer { success ->
            customProgressDialog.dismiss()
            if (success == true) {
                when (intent.getSerializableExtra("email_sender")) {
                    EmailSender.LoginSigninActivity -> {
                        val intent = Intent(this@MailVerification,Welcome::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
                    }
                    EmailSender.ForgotPasswordActivity -> {
                        val intent = Intent(this@MailVerification,ResetPassword::class.java)
                        intent.putExtra("email",email)
                        intent.putExtra("code",binding.pinview.text.toString())
                        startActivity(intent)
                        overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
                    }
                }
            }
        })

        viewModel.errorVerifyLiveData.observe(this,Observer{message->
            customProgressDialog.dismiss()
            if (message != ""){
                Toast.makeText(this@MailVerification,message,Toast.LENGTH_LONG).show()
            }

        })
    }
}