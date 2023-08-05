package tr.main.elephantapps_sprint1.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.databinding.ActivityResetPasswordBinding
import tr.main.elephantapps_sprint1.model.request.PasswordResetModel
import tr.main.elephantapps_sprint1.util.Utils
import tr.main.elephantapps_sprint1.viewmodel.ForgotPasswordViewModel
import tr.main.elephantapps_sprint1.viewmodel.PasswordResetViewModel

class ResetPassword : BaseActivity() {
    private lateinit var binding : ActivityResetPasswordBinding
    private lateinit var email: String
    private lateinit var code: String
    private lateinit var customProgressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customProgressDialog = Dialog(this)
        customProgressDialog.setContentView(R.layout.dialog_custom_progress)
        customProgressDialog.setCanceledOnTouchOutside(false);
        customProgressDialog.setCancelable(false)

        setSupportActionBar(binding.toolbarResetPassword)

        if (supportActionBar != null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarResetPassword.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.toolbarResetPassword.setNavigationIcon(R.drawable.baseline_arrow_back_ios_24)

        binding.txtGiveUp.setOnClickListener {
            onBackPressed()
        }

        email = intent.getStringExtra("email").toString()
        code = intent.getStringExtra("code").toString()

        binding.btnSend.setOnClickListener {

            if(binding.etPassword.text.toString()  == binding.etPasswordAgain.text.toString()){
                changePassword()
            }else{
                Toast.makeText(this@ResetPassword,"Parolalar eşleşmiyor.",Toast.LENGTH_LONG).show()
            }
        }


    }

    private fun changePassword(){
        customProgressDialog.show()
        val viewModel = ViewModelProvider(this).get(PasswordResetViewModel::class.java)


        var passwordResetMode = PasswordResetModel(
            email,
            code,
            binding.etPassword.text.toString()
        )

        viewModel.callApiForPasswordReset(passwordResetMode)

        viewModel.successLiveData.observe(this, Observer { success ->
            customProgressDialog.dismiss()
            if (success == true) {
                val intent = Intent(this@ResetPassword,LoginAndSignin::class.java)
                intent.putExtra("finished",1)
                startActivity(intent)
                finish()
            }
        })

        viewModel.errorLiveData.observe(this,Observer{message->
            customProgressDialog.dismiss()
            if (message != ""){
                Toast.makeText(this@ResetPassword,message,Toast.LENGTH_LONG).show()
            }

        })
    }

}