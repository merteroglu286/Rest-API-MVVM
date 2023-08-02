package tr.main.elephantapps_sprint1.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.databinding.ActivityMailVerificationBinding
import tr.main.elephantapps_sprint1.model.request.VerifyCodeModel
import tr.main.elephantapps_sprint1.util.EmailSender
import tr.main.elephantapps_sprint1.viewmodel.VerifyCodeViewModel

class MailVerification : AppCompatActivity() {

    private lateinit var binding: ActivityMailVerificationBinding
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMailVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbarMailVerification)

        if (supportActionBar != null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarMailVerification.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.toolbarMailVerification.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24)

        binding.txtSendCodeAgain.setOnClickListener{
            Toast.makeText(this,"Kod yeniden gönderilecek",Toast.LENGTH_SHORT).show()
        }

        email = intent.getStringExtra("email").toString()

        binding.btnSend.setOnClickListener {
            callApiForVerifyCode()
        }

        binding.txtSendCodeAgain.setOnClickListener{
            sendCodeAgain()
        }

    }

    private fun callApiForVerifyCode(){

        val viewModel = ViewModelProvider(this).get(VerifyCodeViewModel::class.java)

        val verifyCodeModel = VerifyCodeModel(
            email,
            binding.pinview.text.toString()
        )


        viewModel.getStatusCodeForVerify(verifyCodeModel)

        viewModel.statusCodeVerifyLiveData.observe(this, Observer { statusCode ->
            if (statusCode == 200) {
                println(statusCode)

                when (intent.getSerializableExtra("email_sender")) {
                    EmailSender.LoginSigninActivity -> {
                        val intent = Intent(this@MailVerification,Welcome::class.java)
                        startActivity(intent)
                    }
                    EmailSender.ForgotPasswordActivity -> {
                        val intent = Intent(this@MailVerification,ResetPassword::class.java)
                        intent.putExtra("email",email)
                        intent.putExtra("code",binding.pinview.text.toString())
                        startActivity(intent)
                    }
                }

            } else {
                Toast.makeText(this@MailVerification,"İstek, durum koduyla başarısız:" + statusCode,Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun sendCodeAgain(){

    }
}