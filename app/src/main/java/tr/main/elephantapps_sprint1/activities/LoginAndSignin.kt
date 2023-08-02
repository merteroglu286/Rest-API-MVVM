package tr.main.elephantapps_sprint1.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.marginTop
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.databinding.ActivityLoginAndSigninBinding
import tr.main.elephantapps_sprint1.model.request.SocialAuthenticationModel
import tr.main.elephantapps_sprint1.model.request.UserLoginModel
import tr.main.elephantapps_sprint1.model.request.UserModel
import tr.main.elephantapps_sprint1.util.Constans
import tr.main.elephantapps_sprint1.util.EmailSender
import tr.main.elephantapps_sprint1.util.SocialAuthenticationPlatform
import tr.main.elephantapps_sprint1.viewmodel.SigninAndLoginViewModel


class LoginAndSignin : AppCompatActivity() {


    private lateinit var binding: ActivityLoginAndSigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityLoginAndSigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fullname = binding.etUsername.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (intent.getIntExtra("finished",0) == 1){
            binding.rgToggle.check(R.id.login)
            binding.etUsername.visibility = View.GONE
            binding.btnSignin.visibility = View.GONE
            binding.btnLogin.visibility = View.VISIBLE
            binding.txtForgotPassword.visibility = View.VISIBLE
            binding.txtContract.visibility = View.GONE
        }else{
            binding.rgToggle.check(R.id.signin)
            binding.etUsername.visibility = View.VISIBLE
            binding.btnSignin.visibility = View.VISIBLE
            binding.btnLogin.visibility = View.GONE
            binding.txtForgotPassword.visibility = View.GONE
            binding.txtContract.visibility = View.VISIBLE
        }

        binding.rgToggle.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.signin -> {
                    binding.etUsername.visibility = View.VISIBLE
                    binding.btnSignin.visibility = View.VISIBLE
                    binding.btnLogin.visibility = View.GONE
                    binding.txtForgotPassword.visibility = View.GONE
                    binding.txtContract.visibility = View.VISIBLE

                }
                R.id.login -> {
                    binding.etUsername.visibility = View.GONE
                    binding.btnSignin.visibility = View.GONE
                    binding.btnLogin.visibility = View.VISIBLE
                    binding.txtForgotPassword.visibility = View.VISIBLE
                    binding.txtContract.visibility = View.GONE
                }
            }
        })

        binding.txtForgotPassword.setOnClickListener{
            startActivity(Intent(this@LoginAndSignin,ForgotPassword::class.java))
        }

        binding.btnSignin.setOnClickListener {
            if(binding.etUsername.text.toString() != "" && binding.etEmail.text.toString() != "" && binding.etPassword.text.toString() != ""){
                signIn()
            }
        }


        binding.btnLogin.setOnClickListener {
            if(binding.etEmail.text.toString() != "" &&
                binding.etPassword.text.toString() != ""){
                login()
            }
        }

        binding.llBtnGoogle.setOnClickListener {
            loginWithGoogle()
        }

    }

    private fun signIn(){

        val viewModel = ViewModelProvider(this).get(SigninAndLoginViewModel::class.java)

        val userModel = UserModel(
            fullName = binding.etUsername.text.toString(),
            email = binding.etEmail.text.toString(),
            password = binding.etPassword.text.toString(),
            isOpenNotification = true
        )


        viewModel.getStatusCodeForSignin(userModel)

        viewModel.successSigninLiveData.observe(this, Observer { success ->
            if (success == true) {
                val intent = Intent(this@LoginAndSignin,MailVerification::class.java)
                intent.putExtra("email_sender",EmailSender.LoginSigninActivity)
                intent.putExtra("email",binding.etEmail.text.toString())
                startActivity(intent)
            }
        })

        viewModel.errorSigninLiveData.observe(this,Observer{message->
            if (message != ""){
                Toast.makeText(this@LoginAndSignin,message,Toast.LENGTH_LONG).show()
            }

        })

    }

    private fun login(){
        val viewModel = ViewModelProvider(this).get(SigninAndLoginViewModel::class.java)

        val userLoginModel = UserLoginModel(
            email = binding.etEmail.text.toString(),
            password = binding.etPassword.text.toString()
        )

        viewModel.getStatusCodeForLogin(userLoginModel)

        viewModel.successLoginLiveData.observe(this, Observer { success ->
            if (success == true) {
                val intent = Intent(this@LoginAndSignin,AnaSayfa::class.java)
                startActivity(intent)
                finish()
            }
        })

        viewModel.errorLoginLiveData.observe(this,Observer{message->
            if (message != ""){
                Toast.makeText(this@LoginAndSignin,message,Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun loginWithGoogle(){
        val viewModel = ViewModelProvider(this).get(SigninAndLoginViewModel::class.java)

        val socialAuthenticationModel = SocialAuthenticationModel(
            Constans.TOKEN,
            SocialAuthenticationPlatform.Google
        )

        viewModel.callApiForLoginWithGoogle(socialAuthenticationModel)

        viewModel.successLoginWithGoogleLiveData.observe(this, Observer { success ->
            if (success == true) {
                val intent = Intent(this@LoginAndSignin,AnaSayfa::class.java)
                startActivity(intent)
                finish()
            }
        })

        viewModel.errorLoginWithGoogleLiveData.observe(this,Observer{message->
            if (message != ""){
                Toast.makeText(this@LoginAndSignin,message,Toast.LENGTH_LONG).show()
            }

        })
    }
}
